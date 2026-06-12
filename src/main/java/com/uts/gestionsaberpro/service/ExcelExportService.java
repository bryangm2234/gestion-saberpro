package com.uts.gestionsaberpro.service;

import com.uts.gestionsaberpro.entity.Estudiante;
import com.uts.gestionsaberpro.entity.ResultadoSaberPro;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] generarInformeGeneral(List<Estudiante> estudiantes) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Informe General");
            sheet.setColumnWidth(0, 3000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 7000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 3500);
            sheet.setColumnWidth(5, 7000);
            sheet.setColumnWidth(6, 3000);
            sheet.setColumnWidth(7, 3000);

            CellStyle headerStyle = crearEstiloHeader(wb);
            CellStyle dataStyle   = crearEstiloDato(wb);
            CellStyle numStyle    = crearEstiloNumero(wb);

            Row titulo = sheet.createRow(0);
            Cell cTitulo = titulo.createCell(0);
            cTitulo.setCellValue("INFORME GENERAL DE ALUMNOS - SABER PRO");
            CellStyle tStyle = wb.createCellStyle();
            XSSFFont tf = wb.createFont();
            tf.setBold(true);
            tf.setFontHeightInPoints((short) 14);
            tf.setColor(IndexedColors.DARK_RED.getIndex());
            tStyle.setFont(tf);
            cTitulo.setCellStyle(tStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            String[] headers = {"N°", "Documento", "Nombre Completo", "Programa",
                    "Semestre", "Nº Registro", "Estado", "Resultados"};
            Row hRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell c = hRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 3;
            int n = 1;
            for (Estudiante e : estudiantes) {
                Row row = sheet.createRow(rowIdx++);
                crearCelda(row, 0, String.valueOf(n++), dataStyle);
                crearCelda(row, 1, e.getUsuario().getDocumento(), dataStyle);
                crearCelda(row, 2, e.getUsuario().getNombreCompleto(), dataStyle);
                crearCelda(row, 3, e.getPrograma() != null ? e.getPrograma().getNombre() : "", dataStyle);
                crearCelda(row, 4, String.valueOf(e.getSemestre()), numStyle);
                crearCelda(row, 5, e.getNumeroRegistro() != null ? e.getNumeroRegistro() : "", dataStyle);
                crearCelda(row, 6, Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo", dataStyle);
                crearCelda(row, 7, String.valueOf(e.getResultados().size()), numStyle);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generarInformeDetallado(List<ResultadoSaberPro> resultados) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Resultados Detallados");

            CellStyle headerStyle = crearEstiloHeader(wb);
            CellStyle dataStyle   = crearEstiloDato(wb);
            CellStyle numStyle    = crearEstiloNumero(wb);
            CellStyle supStyle    = crearEstiloSuperior(wb);

            String[] headers = {"Documento", "Estudiante", "Programa",
                    "Puntaje Global", "Nivel", "Com. Escrita", "Raz. Cuantitativo",
                    "Lect. Critica", "Ingles", "Fecha Examen", "Beneficio"};

            int[] anchos = {4500, 8000, 6000, 3500, 3500, 3500, 4000, 3500, 3000, 3500, 9000};
            for (int i = 0; i < anchos.length; i++) sheet.setColumnWidth(i, anchos[i]);

            Row hRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = hRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (ResultadoSaberPro r : resultados) {
                Row row = sheet.createRow(rowIdx++);
                Estudiante e = r.getEstudiante();
                boolean esSup = r.getNivelGlobal() == ResultadoSaberPro.NivelDesempeno.SUPERIOR;
                CellStyle cs = esSup ? supStyle : dataStyle;

                crearCelda(row, 0, e.getUsuario().getDocumento(), cs);
                crearCelda(row, 1, e.getUsuario().getNombreCompleto(), cs);
                crearCelda(row, 2, e.getPrograma() != null ? e.getPrograma().getNombre() : "", cs);
                crearCelda(row, 3, r.getPuntajeGlobal() != null ? r.getPuntajeGlobal().toPlainString() : "-", numStyle);
                crearCelda(row, 4, r.getNivelGlobal() != null ? r.getNivelGlobal().name() : "-", cs);
                crearCelda(row, 5, val(r.getComunicacionEscrita()), numStyle);
                crearCelda(row, 6, val(r.getRazonamientoCuantitativo()), numStyle);
                crearCelda(row, 7, val(r.getLecturaCritica()), numStyle);
                crearCelda(row, 8, val(r.getIngles()), numStyle);
                crearCelda(row, 9, r.getFechaExamen() != null ? r.getFechaExamen().toString() : "-", dataStyle);
                crearCelda(row, 10, r.getDescripcionBeneficio() != null ? r.getDescripcionBeneficio() : "Sin beneficio", cs);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    private void crearCelda(Row row, int col, String valor, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(valor != null ? valor : "");
        c.setCellStyle(style);
    }

    private String val(java.math.BigDecimal bd) {
        return bd != null ? bd.toPlainString() : "-";
    }

    private CellStyle crearEstiloHeader(XSSFWorkbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setBorderBottom(BorderStyle.THIN);
        return s;
    }

    private CellStyle crearEstiloDato(XSSFWorkbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setBorderBottom(BorderStyle.HAIR);
        s.setBorderRight(BorderStyle.HAIR);
        return s;
    }

    private CellStyle crearEstiloNumero(XSSFWorkbook wb) {
        CellStyle s = crearEstiloDato(wb);
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }

    private CellStyle crearEstiloSuperior(XSSFWorkbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setBorderBottom(BorderStyle.HAIR);
        return s;
    }
}
