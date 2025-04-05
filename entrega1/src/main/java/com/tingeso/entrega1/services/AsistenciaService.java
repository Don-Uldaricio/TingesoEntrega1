package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AsistenciaService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public boolean importarAsistencia(MultipartFile file) {
        if (isExcelFile(file)) {
            try {
                List<AsistenciaData> asistenciaList = procesarExcel(file.getInputStream());
                guardarAsistencia(asistenciaList);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
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
        System.out.println("Iniciando procesamiento del Excel");

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            System.out.println("Workbook creado correctamente");
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Número de filas en la hoja: " + sheet.getPhysicalNumberOfRows());

            // Procesamos cada fila como un estudiante diferente
            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                System.out.println("\n==== Procesando fila " + rowIndex + " (estudiante) ====");

                if (currentRow == null) {
                    System.out.println("Fila " + rowIndex + " es nula, saltando");
                    continue;
                }

                Cell rutCell = currentRow.getCell(0);
                System.out.println("Celda 0 (RUT) es null? " + (rutCell == null));

                // Verificamos que la celda del RUT no sea nula
                if (rutCell == null) {
                    System.out.println("Fila " + rowIndex + ": Celda de RUT es nula, saltando fila");
                    continue;
                }

                // Mostramos el tipo de la celda del RUT
                System.out.println("Tipo de celda de RUT: " + rutCell.getCellType());

                // Extraemos el RUT manejando diferentes tipos de celda
                String rut = obtenerValorCelda(rutCell);
                System.out.println("RUT obtenido: " + rut);

                if (rut.isEmpty()) {
                    System.out.println("Fila " + rowIndex + ": RUT está vacío, saltando fila");
                    continue;
                }

                AsistenciaData asistenciaData = new AsistenciaData();
                asistenciaData.setRut(rut);
                asistenciaData.setAsistenciaMensual(new ArrayList<>());

                System.out.println("Procesando datos de asistencia para RUT: " + rut);
                // Procesamos las 12 columnas de asistencia (columnas 1-12)
                for (int colIndex = 1; colIndex <= 12; colIndex++) {
                    Cell cell = currentRow.getCell(colIndex);
                    float porcentaje = 0.0f;
                    System.out.print("Columna " + colIndex + ": ");

                    if (cell != null) {
                        System.out.print("Tipo=" + cell.getCellType() + ", ");
                        try {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                porcentaje = (float) cell.getNumericCellValue();
                                System.out.print("Valor numérico=" + porcentaje);
                            } else if (cell.getCellType() == CellType.STRING) {
                                String valor = cell.getStringCellValue().trim().replace(',', '.');
                                porcentaje = Float.parseFloat(valor);
                                System.out.print("Valor string=" + valor + ", convertido a=" + porcentaje);
                            } else {
                                System.out.print("Tipo no procesable");
                            }
                        } catch (Exception e) {
                            // Si hay un error, asumimos 0
                            System.out.print("ERROR al obtener valor: " + e.getMessage());
                            porcentaje = 0.0f;
                        }
                    } else {
                        System.out.print("Celda nula");
                    }
                    System.out.println(" -> Porcentaje final=" + porcentaje);
                    asistenciaData.getAsistenciaMensual().add(porcentaje);
                }

                System.out.println("Lista de asistencia para RUT " + rut + ": " + asistenciaData.getAsistenciaMensual());
                asistenciaDataList.add(asistenciaData);
            }
        }

        System.out.println("Fin del procesamiento del Excel");
        System.out.println("Total de registros de asistencia procesados: " + asistenciaDataList.size());
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
                // Para números, usamos el formato bruto para evitar notación científica
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    double valor = cell.getNumericCellValue();
                    // Si es un entero, quitar la parte decimal
                    if (valor == Math.floor(valor)) {
                        return String.format("%.0f", valor);
                    } else {
                        return String.valueOf(valor);
                    }
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
                // Limpiamos cualquier dato previo de asistencia
                estudiante.getAsistenciaMensual().clear();

                // Agregamos los nuevos datos de asistencia
                estudiante.getAsistenciaMensual().addAll(asistenciaData.getAsistenciaMensual());

                // Aseguramos que la lista tenga exactamente 12 elementos
                while (estudiante.getAsistenciaMensual().size() < 12) {
                    estudiante.getAsistenciaMensual().add(0.0f);
                }

                // Guardamos el estudiante actualizado
                estudianteRepository.save(estudiante);

                // Registrar en el log para depuración
                System.out.println("Asistencia guardada para el estudiante con RUT: " + asistenciaData.getRut());
                System.out.println("Número de valores de asistencia: " + estudiante.getAsistenciaMensual().size());
                System.out.println("Valores de asistencia: " + estudiante.getAsistenciaMensual());
            } else {
                System.out.println("No se encontró estudiante con RUT: " + asistenciaData.getRut());
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