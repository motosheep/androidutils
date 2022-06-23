package com.north.light.androidutils.filetype;

/**
 * FileName: FileType
 * Author: lzt
 * Date: 2022/5/17 15:11
 * 文件类型--常量类
 */
public final class FileType {

    /**
     * 常用文件的文件头如下：(以前六位为准)
     * JPEG (jpg)，文件头：FFD8FF
     * PNG (png)，文件头：89504E47
     * GIF (gif)，文件头：47494638
     * TIFF (tif)，文件头：49492A00
     * Windows Bitmap (bmp)，文件头：424D
     * webp (webp)，文件头：52494646
     * CAD (dwg)，文件头：41433130
     * Adobe Photoshop (psd)，文件头：38425053
     * Rich Text Format (rtf)，文件头：7B5C727466
     * XML (xml)，文件头：3C3F786D6C
     * HTML (html)，文件头：68746D6C3E
     * Email [thorough only] (eml)，文件头：44656C69766572792D646174653A
     * Outlook Express (dbx)，文件头：CFAD12FEC5FD746F
     * Outlook (pst)，文件头：2142444E
     * MS Word/Excel (xls.or.doc)，文件头：D0CF11E0
     * MS Access (mdb)，文件头：5374616E64617264204A
     * WordPerfect (wpd)，文件头：FF575043
     * Postscript (eps.or.ps)，文件头：252150532D41646F6265
     * Adobe Acrobat (pdf)，文件头：255044462D312E
     * Quicken (qdf)，文件头：AC9EBD8F
     * Windows Password (pwl)，文件头：E3828596
     * ZIP Archive (zip)，文件头：504B0304
     * RAR Archive (rar)，文件头：52617221
     * Wave (wav)，文件头：57415645
     * AVI (avi)，文件头：41564920
     * Real Audio (ram)，文件头：2E7261FD
     * Real Media (rm)，文件头：2E524D46
     * MPEG (mpg)，文件头：000001BA
     * MPEG (mpeg)，文件头：000001B3
     * Quicktime (mov)，文件头：6D6F6F76
     * Windows Media (asf)，文件头：3026B2758E66CF11
     * MIDI (mid)，文件头：4D546864
     */

    public static final String TYPE_JPG = ".jpg";
    public static final String TYPE_JPG_HEAD = "FFD8FF";
    public static final String TYPE_GIF = ".gif";
    public static final String TYPE_GIF_HEAD = "47494638";
    public static final String TYPE_PNG = ".png";
    public static final String TYPE_PNG_HEAD = "89504E47";
    public static final String TYPE_BMP = ".bmp";
    public static final String TYPE_BMP_HEAD = "424D";
    public static final String TYPE_WEBP = ".webp";
    public static final String TYPE_WEBP_HEAD = "52494646";
    public static final String TYPE_TIF = ".tif";
    public static final String TYPE_TIF_HEAD = "49492A00";
    public static final String TYPE_CAD = ".cad";
    public static final String TYPE_CAD_HEAD = "41433130";

    public static final String TYPE_DWG = ".dwg";
    public static final String TYPE_DWG_HEAD = "41433130";
    public static final String TYPE_PSD = ".psd";
    public static final String TYPE_PSD_HEAD = "38425053";
    public static final String TYPE_RTF = ".rtf";
    public static final String TYPE_RTF_HEAD = "7B5C727466";
    public static final String TYPE_XML = ".xml";
    public static final String TYPE_XML_HEAD = "3C3F786D6C";
    public static final String TYPE_HTML = ".html";
    public static final String TYPE_HTML_HEAD = "68746D6C3E";
    public static final String TYPE_EML = ".eml";
    public static final String TYPE_EML_HEAD = "44656C69766572792D646174653A";
    public static final String TYPE_DBX = ".dbx";
    public static final String TYPE_DBX_HEAD = "CFAD12FEC5FD746F";
    public static final String TYPE_PST = ".pst";
    public static final String TYPE_PST_HEAD = "2142444E";
    public static final String TYPE_WORD_OR_EXCEL = ".xls.or.doc";
    public static final String TYPE_WORD_OR_EXCEL_HEAD = "D0CF11E0";
    public static final String TYPE_MDB = ".mdb";
    public static final String TYPE_MDB_HEAD = "5374616E64617264204A";
    public static final String TYPE_WPD = ".wpd";
    public static final String TYPE_WPD_HEAD = "FF575043";
    public static final String TYPE_EPS_OR_PS = ".eps.or.ps";
    public static final String TYPE_EPS_OR_PS_HEAD = "252150532D41646F6265";
    public static final String TYPE_PDF = ".pdf";
    public static final String TYPE_PDF_HEAD = "255044462D312E";
    public static final String TYPE_QDF = ".qdf";
    public static final String TYPE_QDF_HEAD = "AC9EBD8F";
    public static final String TYPE_PWL = ".pwl";
    public static final String TYPE_PWL_HEAD = "E3828596";
    public static final String TYPE_ZIP = ".zip";
    public static final String TYPE_ZIP_HEAD = "504B0304";
    public static final String TYPE_RAR = ".rar";
    public static final String TYPE_RAR_HEAD = "52617221";
    public static final String TYPE_WAV = ".wav";
    public static final String TYPE_WAV_HEAD = "57415645";
    public static final String TYPE_AVI = ".avi";
    public static final String TYPE_AVI_HEAD = "41564920";
    public static final String TYPE_RAM = ".ram";
    public static final String TYPE_RAM_HEAD = "2E7261FD";
    public static final String TYPE_RM = ".rm";
    public static final String TYPE_RM_HEAD = "2E524D46";
    public static final String TYPE_MPG = ".mpg";
    public static final String TYPE_MPG_HEAD = "000001BA";
    public static final String TYPE_MPEG = ".mpeg";
    public static final String TYPE_MPEG_HEAD = "000001B3";
    public static final String TYPE_MOV = ".mov";
    public static final String TYPE_MOV_HEAD = "6D6F6F76";
    public static final String TYPE_ASF = ".asf";
    public static final String TYPE_ASF_HEAD = "3026B2758E66CF11";
    public static final String TYPE_MID = ".mid";
    public static final String TYPE_MID_HEAD = "4D546864";

    public static final String TYPE_UNKNOWN = "unknown";

}
