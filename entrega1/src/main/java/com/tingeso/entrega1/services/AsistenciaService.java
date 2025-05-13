package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AsistenciaService {

    private final EstudianteRepository estudianteRepository;
    private static final Logger logger = LoggerFactory.getLogger(AsistenciaService.class);
    private static final String PREFIX_FILA = "Fila ";

    public AsistenciaService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public boolean importarAsistencia(MultipartFile file) {
        if (isExcelFile(file)) {
            try {
                List<AsistenciaData> asistenciaList = procesarExcel(file.getInputStream());
                guardarAsistencia(asistenciaList);
                return true;
            } catch (IOException e) {
                logger.error("Error al procesar el archivo Excel", e);
                return false;
            }
        }
        return false;
    }

    private boolean isExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                    contentType.equals("application/vnd.ms-excel");
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            return filename.toLowerCase().endsWith(".xlsx") ||
                    filename.toLowerCase().endsWith(".xls");
        }

        return false;
    }

    private List<AsistenciaData> procesarExcel(InputStream inputStream) throws IOException {
        List<AsistenciaData> asistenciaDataList = new ArrayList<>();
        logger.info("Iniciando procesamiento del Excel");

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            logger.info("Workbook creado correctamente");
            Sheet sheet = workbook.getSheetAt(0);
            logger.info("Número de filas en la hoja: {}", sheet.getPhysicalNumberOfRows());

            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                logger.info("\n==== {}{} (estudiante) ====", PREFIX_FILA, rowIndex);

                if (currentRow == null) {
                    logger.warn("{}{} es nula, saltando", PREFIX_FILA, rowIndex);
                    continue;
                }

                Cell rutCell = currentRow.getCell(0);
                logger.debug("Celda 0 (RUT) es null? {}", rutCell == null);

                if (rutCell == null) {
                    logger.warn("{}{}: Celda de RUT es nula, saltando fila", PREFIX_FILA, rowIndex);
                    continue;
                }

                logger.debug("Tipo de celda de RUT: {}", rutCell.getCellType());

                String rut = obtenerValorCelda(rutCell);
                logger.info("RUT obtenido: {}", rut);

                if (rut.isEmpty()) {
                    logger.warn("{}{}: RUT está vacío, saltando fila", PREFIX_FILA, rowIndex);
                    continue;
                }

                AsistenciaData asistenciaData = new AsistenciaData();
                asistenciaData.setRut(rut);
                asistenciaData.setAsistenciaMensual(new ArrayList<>());

                logger.info("Procesando datos de asistencia para RUT: {}", rut);

                for (int colIndex = 1; colIndex <= 12; colIndex++) {
                    Cell cell = currentRow.getCell(colIndex);
                    float porcentaje = 0.0f;
                    logger.debug("Columna {}: ", colIndex);

                    if (cell != null) {
                        try {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                porcentaje = (float) cell.getNumericCellValue();
                                logger.debug("Valor numérico={}", porcentaje);
                            } else if (cell.getCellType() == CellType.STRING) {
                                String valor = cell.getStringCellValue().trim().replace(',', '.');
                                porcentaje = Float.parseFloat(valor);
                                logger.debug("Valor string='{}', convertido a={}", valor, porcentaje);
                            } else {
                                logger.debug("Tipo no procesable");
                            }
                        } catch (Exception e) {
                            logger.warn("Error al obtener valor: {}", e.getMessage());
                            porcentaje = 0.0f;
                        }
                    } else {
                        logger.debug("Celda nula");
                    }
                    logger.debug("Porcentaje final={}", porcentaje);
                    asistenciaData.getAsistenciaMensual().add(porcentaje);
                }

                logger.info("Lista de asistencia para RUT {}: {}", rut, asistenciaData.getAsistenciaMensual());
                asistenciaDataList.add(asistenciaData);
            }
        }

        logger.info("Fin del procesamiento del Excel");
        logger.info("Total de registros de asistencia procesados: {}", asistenciaDataList.size());
        return asistenciaDataList;
    }

    private String obtenerValorCelda(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    double valor = cell.getNumericCellValue();
                    return (valor == Math.floor(valor)) ? String.format("%.0f", valor) : String.valueOf(valor);
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return "";
                    }
                }
            default:
                return "";
        }
    }

    private void guardarAsistencia(List<AsistenciaData> asistenciaDataList) {
        for (AsistenciaData asistenciaData : asistenciaDataList) {
            Estudiante estudiante = estudianteRepository.findByRut(asistenciaData.getRut());

            if (estudiante != null) {
                estudiante.getAsistenciaMensual().clear();
                estudiante.getAsistenciaMensual().addAll(asistenciaData.getAsistenciaMensual());

                while (estudiante.getAsistenciaMensual().size() < 12) {
                    estudiante.getAsistenciaMensual().add(0.0f);
                }

                estudianteRepository.save(estudiante);

                logger.info("Asistencia guardada para el estudiante con RUT: {}", asistenciaData.getRut());
                logger.info("Número de valores de asistencia: {}", estudiante.getAsistenciaMensual().size());
                logger.info("Valores de asistencia: {}", estudiante.getAsistenciaMensual());
            } else {
                logger.warn("No se encontró estudiante con RUT: {}", asistenciaData.getRut());
            }
        }
    }

    // Clase interna para manejar los datos de asistencia
    private static class AsistenciaData {
        private String rut;
        private List<Float> asistenciaMensual;

        public String getRut() {
            return rut;
        }

        public void setRut(String rut) {
            this.rut = rut;
        }

        public List<Float> getAsistenciaMensual() {
            return asistenciaMensual;
        }

        public void setAsistenciaMensual(List<Float> asistenciaMensual) {
            this.asistenciaMensual = asistenciaMensual;
        }
    }
}
