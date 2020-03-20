
# react-native-lad-print-pdf

## Getting started

`$ npm install react-native-lad-print-pdf --save`

### Mostly automatic installation

`$ react-native link react-native-lad-print-pdf`

### Manual installation



#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNNetworkInfoPackage;` to the imports at the top of the file
  - Add `new RNNetworkInfoPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-network-info'
  	project(':react-native-network-info').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-network-info/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-network-info')
  	```

## Usage
```javascript
import {  RNLadPdfPrint } from 'react-native-lad-print-pdf';
(async ()=>{
  const saveData = await RNLadPdfPrint.saveBase64ToPdf('name','base64');
  console.log('saveBase64ToPdf',saveData);
  const printData = await RNLadPdfPrint.saveBase64ToPdf('path/to/pdf/file');
  console.log('printPdf',printData);
  const printPdfBase64Data = await RNLadPdfPrint.printPdfBase64('base64-pdf-content');
  console.log('printPdfBase64',printPdfBase64Data)
})();

```
  