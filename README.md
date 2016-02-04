![MIT-License](https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat-square)

Ninja Printer
=============

Ninja Printer is a browser based solution for printing from authorised website(s) using Google Chrome. Technically the
solution is based on the NativeMessaging feature of the browser and runs a simple Java application in the background to
execute printing on the local machine. Currently it only supports Windows and OSX, but it should be easy to adjust other
unix-like systems.

One of the main design goals was to be able to pick the printer to be used from the web application, meaning obviously
that the application needs to be aware of the available printers on the users machine. NinjaPrinter ships with support
for 2 different type of documents: ZPL (label) and the PDF, but extending it should be fairly straight forward.

Security & Customisation
------------------------

For obvious security reasons, you will need to modify the `content_scripts` to match your needs in order to use it.
The web application will also need to be aware of the name of the available printers for the document type that is
to be printed.

Installation
------------

Installation instructions for Ninja Printer.

### On Windows

#### 1. Install the extension 

- Download the zip file from the dropbox link above
 - Open chrome and click on the ‘Tools’ icon on the right hand corner
 - Select ‘Extensions’
 - On the top right hand side tick ‘Developer mode’
 - Click ‘Load unpacked extension’
 - Select the ‘browser’ folder inside the original file ‘chrome-extension’
 - You will see the extension already enabled in your list of chrome extension

**Note:** No need to tick ‘allow on incognito mode’ as you will always need to enable the extension on the incognito window.

#### 2. Install the host 

 - Go to chrome-extension\host
 - Double click: install_host.bat

### On OSX

#### 1. Install the extension 

 - Download the zip file from the dropbox link above
 - Open chrome and click on the hamburger menu on the right hand corner 
 - Go to 'More tools' > ‘Extensions’
 - On the top right hand side tick ‘Developer mode’
 - Click ‘Load unpacked extension’
 - Select the ‘app’ folder inside the original file ‘chrome-extension’
 - You will see the extension already enabled in your list of chrome extension

**Note:** No need to tick ‘allow on incognito mode’ as you will always need to enable the extension on the incognito window.

#### 2. Install the host

 - Go to chrome-extension/host
 - Double click: install_host.sh


Usage
-----

The extension will inject NinjaPrinter.js into your website which containes the NinjaPrinter JavaScript object.

The NinjaPrinter object defines:
 -  3 methods for printing:
   - print
   - printZpl
   - printPdf
 - 3 events to listen to:
   - ninjaprinter.result
   - ninjaprinter.beforePrint
   - ninjaprinter.afterPrint
 - 2 methods to manage event subscription:
   - on
   - off

For some minimalist usage examples, please check out the contents of the examples directory.

