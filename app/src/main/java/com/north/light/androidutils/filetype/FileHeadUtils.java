package com.north.light.androidutils.filetype;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FileName: FileHeadUtils
 * Author: lzt
 * Date: 2022/5/17 15:03
 */
public class FileHeadUtils {

    /**
     * 常用文件的文件头如下：(以前六位为准)
     * JPEG (jpg)，文件头：FFD8FF
     * PNG (png)，文件头：89504E47
     * GIF (gif)，文件头：47494638
     * TIFF (tif)，文件头：49492A00
     * Windows Bitmap (bmp)，文件头：424D
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
     * MPEG (mpg)，文件头：000001B3
     * Quicktime (mov)，文件头：6D6F6F76
     * Windows Media (asf)，文件头：3026B2758E66CF11
     * MIDI (mid)，文件头：4D546864
     */

    public static final class SingleHolder {
        static FileHeadUtils mInstance = new FileHeadUtils();
    }

    public static FileHeadUtils getInstance() {
        return SingleHolder.mInstance;
    }

    //内部调用----------------------------------------------------------------------------------


    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    private String bytesToHexString(byte[] src) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 根据文件流判断类型
     *
     * @param fis
     */
    private String getPicTypeInner(InputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = bytesToHexString(b).toUpperCase();
            if (type.contains(FileType.TYPE_JPG_HEAD)) {
                return FileType.TYPE_JPG;
            } else if (type.contains(FileType.TYPE_PNG_HEAD)) {
                return FileType.TYPE_PNG;
            } else if (type.contains(FileType.TYPE_GIF_HEAD)) {
                return FileType.TYPE_GIF;
            } else if (type.contains(FileType.TYPE_BMP_HEAD)) {
                return FileType.TYPE_BMP;
            } else if (type.contains(FileType.TYPE_WEBP_HEAD)) {
                return FileType.TYPE_WEBP;
            } else if (type.contains(FileType.TYPE_TIF_HEAD)) {
                return FileType.TYPE_TIF;
            } else if (type.contains(FileType.TYPE_CAD_HEAD)) {
                return FileType.TYPE_CAD;
            } else if (type.contains(FileType.TYPE_DWG_HEAD)) {
                return FileType.TYPE_DWG;
            } else if (type.contains(FileType.TYPE_PSD_HEAD)) {
                return FileType.TYPE_PSD;
            } else if (type.contains(FileType.TYPE_RTF_HEAD)) {
                return FileType.TYPE_RTF;
            } else if (type.contains(FileType.TYPE_XML_HEAD)) {
                return FileType.TYPE_XML;
            } else if (type.contains(FileType.TYPE_HTML_HEAD)) {
                return FileType.TYPE_HTML;
            } else if (type.contains(FileType.TYPE_EML_HEAD)) {
                return FileType.TYPE_EML;
            } else if (type.contains(FileType.TYPE_DBX_HEAD)) {
                return FileType.TYPE_DBX;
            } else if (type.contains(FileType.TYPE_PST_HEAD)) {
                return FileType.TYPE_PST;
            } else if (type.contains(FileType.TYPE_WORD_OR_EXCEL_HEAD)) {
                return FileType.TYPE_WORD_OR_EXCEL;
            } else if (type.contains(FileType.TYPE_MDB_HEAD)) {
                return FileType.TYPE_MDB;
            } else if (type.contains(FileType.TYPE_WPD_HEAD)) {
                return FileType.TYPE_WPD;
            } else if (type.contains(FileType.TYPE_EPS_OR_PS_HEAD)) {
                return FileType.TYPE_EPS_OR_PS;
            } else if (type.contains(FileType.TYPE_PDF_HEAD)) {
                return FileType.TYPE_PDF;
            } else if (type.contains(FileType.TYPE_QDF_HEAD)) {
                return FileType.TYPE_QDF;
            } else if (type.contains(FileType.TYPE_PWL_HEAD)) {
                return FileType.TYPE_PWL;
            } else if (type.contains(FileType.TYPE_ZIP_HEAD)) {
                return FileType.TYPE_ZIP;
            } else if (type.contains(FileType.TYPE_RAR_HEAD)) {
                return FileType.TYPE_RAR;
            } else if (type.contains(FileType.TYPE_WAV_HEAD)) {
                return FileType.TYPE_WAV;
            } else if (type.contains(FileType.TYPE_AVI_HEAD)) {
                return FileType.TYPE_AVI;
            } else if (type.contains(FileType.TYPE_RAM_HEAD)) {
                return FileType.TYPE_RAM;
            } else if (type.contains(FileType.TYPE_RM_HEAD)) {
                return FileType.TYPE_RM;
            } else if (type.contains(FileType.TYPE_MPG_HEAD)) {
                return FileType.TYPE_MPG;
            } else if (type.contains(FileType.TYPE_MPEG_HEAD)) {
                return FileType.TYPE_MPEG;
            } else if (type.contains(FileType.TYPE_MOV_HEAD)) {
                return FileType.TYPE_MOV;
            } else if (type.contains(FileType.TYPE_ASF_HEAD)) {
                return FileType.TYPE_ASF;
            } else if (type.contains(FileType.TYPE_MID_HEAD)) {
                return FileType.TYPE_MID;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return FileType.TYPE_UNKNOWN;
    }

    //外部调用----------------------------------------------------------------------------------

    /**
     * 根据文件流判断图片类型
     */
    public String getFileType(FileInputStream fis) {
        return getPicTypeInner(fis);
    }

    public String getFileType(InputStream fis) {
        return getPicTypeInner(fis);
    }

}
