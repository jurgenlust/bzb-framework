package be.bzbit.framework.i18n.conversion.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import be.bzbit.framework.i18n.conversion.ConversionService;
import be.bzbit.framework.i18n.dto.Translation;
import be.bzbit.framework.i18n.exception.ConversionException;
import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.service.TranslationService;

public class Excel97ConversionService implements ConversionService, Serializable {

	private static final long serialVersionUID = 4279912071593562347L;
	private static final Log log = LogFactory.getLog(Excel97ConversionService.class);
	
	private TranslationService translationService;

	public Excel97ConversionService() {
		super();
	}

	@Override
	@Transactional(readOnly=true)
	public void exportTranslations(OutputStream outputStream) {
		try {
			HSSFWorkbook workbook = createWorkbook();
			workbook.write(outputStream);
		} catch (Exception e) {
			log.error("cannot export Excel 97 workbook", e);
			throw new ConversionException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional
	public void importTranslations(final InputStream inputStream) {
		try {
			final HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			final HSSFSheet sheet = workbook.getSheetAt(0);
			final Iterator rowIterator = sheet.rowIterator();
			final List<Locale> locales = new ArrayList<Locale>();
			//get the title row
			if (rowIterator.hasNext()) {
				final HSSFRow titleRow = (HSSFRow)rowIterator.next();
				final int numberOfColumns = titleRow.getLastCellNum();
				for (int i = 2; i < numberOfColumns; i++) {
					final HSSFCell localeCell = titleRow.getCell(i);
					if (localeCell == null) continue;
					final String language = localeCell.getStringCellValue();
					if (StringUtils.isNotBlank(language)) {
						final Locale locale = new Locale(language);
						getTranslationService().addLocale(locale);
						locales.add(locale);
					}
				}
			}
			while(rowIterator.hasNext()) {
				final HSSFRow translationRow = (HSSFRow)rowIterator.next();
				final HSSFCell codeCell = translationRow.getCell(0);
				final HSSFCell defaultMessageCell = translationRow.getCell(1);
				if (codeCell == null || defaultMessageCell == null) continue;
				final String messageCode = codeCell.getStringCellValue();
				final String defaultTranslation = defaultMessageCell.getStringCellValue();
				if (StringUtils.isBlank(messageCode) || StringUtils.isBlank(defaultTranslation)) continue;
				final Message defaultMessage = new Message(messageCode.trim());
				defaultMessage.setMessage(defaultTranslation.trim());
				final List<Message> translatedMessages = new ArrayList<Message>();
				translatedMessages.add(defaultMessage);
				for (int i = 0; i < locales.size(); i++) {
					final Message translatedMessage = new Message(messageCode);
					translatedMessage.setLocale(locales.get(i));
					if (translationRow.getCell(i+2) != null) {
						final String translation = translationRow.getCell(i+2).getStringCellValue();
						if (StringUtils.isNotBlank(translation)) {
							translatedMessage.setMessage(translation.trim());
						}
					}
					translatedMessages.add(translatedMessage);
				}
				final Translation translation = new Translation(messageCode, locales, translatedMessages);
				getTranslationService().saveTranslation(translation);
			}
		} catch (IOException e) {
			log.error("Cannot import Excel file with translations", e);
		}

	}
	
	protected HSSFWorkbook createWorkbook() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		initializeColumns(workbook, sheet);
		HSSFCellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setWrapText(true);
		defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

		List<Message> defaultMessages = getTranslationService().getDefaultMessages();
		int rowNumber = 1;
		for (Message message : defaultMessages) {
			log.debug("export message " + message.getCode());
			Translation translation = getTranslationService().getTranslation(message.getCode());
			exportTranslation(sheet, defaultStyle, translation, rowNumber++);
		}
		return workbook;
	}
	
	@SuppressWarnings("deprecation")
	protected void exportTranslation(HSSFSheet sheet, HSSFCellStyle cellStyle, Translation translation, int rowNumber) {
		if (sheet == null) return;
		HSSFRow row = sheet.createRow(rowNumber);
		int column = 0;
		//add the code
		HSSFCell codeCell = row.createCell(column++, HSSFCell.CELL_TYPE_STRING);
		codeCell.setCellStyle(cellStyle);
		codeCell.setCellValue(translation.getMessageCode());
		HSSFCell defaultCell = row.createCell(column++, HSSFCell.CELL_TYPE_STRING);
		defaultCell.setCellStyle(cellStyle);
		defaultCell.setCellValue(translation.getDefaultMessage().getMessage());
		for (Message message : translation.getMessages()) {
			HSSFCell translationCell = row.createCell(column++, HSSFCell.CELL_TYPE_STRING);
			translationCell.setCellStyle(cellStyle);
			translationCell.setCellValue(message.getMessage());
		}
	}
	
	protected void initializeColumns(HSSFWorkbook workbook, HSSFSheet sheet) {
		//create the title row
		HSSFRow titleRow = sheet.createRow(0);
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_HAIR);
		HSSFFont titleFont = workbook.createFont();
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleStyle.setFont(titleFont);
		int column = 0;
		//add the code
		addColumnHeader(workbook, sheet, titleRow, titleStyle, column, "code");
		sheet.setColumnWidth(column++, 20*256);
		//add the default message
		addColumnHeader(workbook, sheet, titleRow, titleStyle, column, "defaultMessage");
		sheet.setColumnWidth(column++, 40*256);
		//add the translation columns
		List<Locale> locales = getTranslationService().getAvailableLocales();
		for (Locale locale : locales) {
			addColumnHeader(workbook, sheet, titleRow, titleStyle, column, locale.getLanguage());
			sheet.setColumnWidth(column++, 40*256);
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void addColumnHeader(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow titleRow, HSSFCellStyle titleStyle, int column, String value) {
		HSSFCell cell = titleRow.createCell(column, HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(titleStyle);
		cell.setCellValue(value);
	}

	public TranslationService getTranslationService() {
		return translationService;
	}

	public void setTranslationService(TranslationService translationService) {
		this.translationService = translationService;
	}
}
