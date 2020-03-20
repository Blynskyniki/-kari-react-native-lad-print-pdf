
import { NativeModules } from 'react-native';
const { RNLadPrintPdf } = NativeModules;
/**
 * Результат сохранения PDF
 * @interface
 */
export interface ISavePdfData {
  path:string;
  save:boolean;
}
/**
 * Результат печати PDF
 * @interface
 */
export interface IPrintPdfData {
  path?:string;
  send:boolean;
}
/**
 * Базовая ошибка
 * @interface
 */
export interface IRNLadPdfPrintError {
  errorCode:string;
  errorMsg:string;
  save:boolean;
  send:boolean;

}
/**
 * @Class RNLadPdfPrint
 * @static
 * @Classdesc Описывает возможности нативного модуля
 * 1) Сохранение PDF файла из base64
 * 2) Печать PDF файла
 * @permission Проверкка на уровне модуля на READ_EXTERNAL_STORAGE и WRITE_EXTERNAL_STORAGE
 */
export class RNLadPdfPrint {

  /**
   *  Сохранение PDF файла из base64
   * @param {string} name - имя файла без приставки формата (.pdf)
   * @param {string} base64 - PDF в формате base64
   * @return {Promise<ISavePdfData | IRNLadPdfPrintError>}
   */
  public static async  saveBase64ToPdf(name:string,base64:string):Promise<ISavePdfData|IRNLadPdfPrintError>{
    return await RNLadPrintPdf.saveBase64ToPdf(name,base64);
  }


  /**
   * Печать PDF файла
   * @param {string} path - путь до файла
   * @return {Promise<IPrintPdfData | IRNLadPdfPrintError>}
   */
  public static async printPdf(path:string):Promise<IPrintPdfData|IRNLadPdfPrintError>{
    return await RNLadPrintPdf.printPdf(path);
  }

  /**
   * Печать PDF файла из base64
   * @param {string} base64 - pdf в формате base64
   * @return {Promise<IPrintPdfData | IRNLadPdfPrintError>}
   */
  public static async printPdfBase64(base64:string):Promise<IPrintPdfData|IRNLadPdfPrintError>{
    return await RNLadPrintPdf.printBase64Pdf(base64);
  }


}


