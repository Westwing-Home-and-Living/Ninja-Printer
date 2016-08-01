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

Since the base implementation only supports *westwing* domains, localhost and 127.0.0.1, you will need to create your
own extension if you intend to use it in production.

### Development

For development purposes it's best to use a local development:

1. Fork this repo (or get the code elsehow)
2. chrome-extension/browser/manifest.json
  1. Find and open file
  2. Amend `content_scripts.matches` to match your needs
  3. Save file
3. [Chrome Extensions page in Google Chrome\*](chrome://extensions/)
  1. Open page
  2. Make sure `Developer Mode` is ticked
  3. Click `Load unpacked extension...`
  4. Navigate to the chrome-extension/browser directory of your source code.
  5. Note the ID of your newly installed extension
4. **For OSX:** chrome-extension/host/de.westwing.chrome.printer.ninja.json
  1. Find and open file
  2. Amend `allowed_origins` to have an entry like "chrome-extension://{YOUR_EXTENSION_ID}/"
  3. Save file
  4. Execute `install_host.sh`
5. **For Windows:** chrome-extension/host/de.westwing.chrome.printer.ninja-win.json
  1. Find and open file
  2. Amend `allowed_origins` to have an entry like "chrome-extension://{YOUR_EXTENSION_ID}/"
  3. Save file
  4. Execute `install_host.bat`

\*: It might work similarly with Chromium and derivative browsers, but has only been tested using Google Chrome.

### Production

For production you'll also need to follow the above steps, but you'll need upload your chrome-extension/browser
directory to [chrome web store](https://chrome.google.com/webstore/category/extensions) and install from there.

Installation
------------

Basic installation instructions for Ninja Printer.

### On Windows

#### 1. Install the extension 

You can always test the provided example application via
[the official extension](https://chrome.google.com/webstore/detail/ninja-printer/fnacfbhdnejbjiglnlfgeaaifcmmmncb).
Or you can setup your development version:

 - Fork or download the source code
 - Open chrome and click on the ‘Tools’ icon on the right hand corner
 - Select ‘Extensions’
 - On the top right hand side tick ‘Developer mode’
 - Click ‘Load unpacked extension’
 - Select the ‘browser’ folder inside the original file ‘chrome-extension’
 - You will see the extension already enabled in your list of chrome extension

**Note:** If you tick ‘allow on incognito mode’ than you will always need to enable the extension on the incognito window.

#### 2. Install the host 

 - Go to chrome-extension\host
 - Double click: install_host.bat

### On OSX

#### 1. Install the extension 

You can always test the provided example application via
[the official extension](https://chrome.google.com/webstore/detail/ninja-printer/fnacfbhdnejbjiglnlfgeaaifcmmmncb).
Or you can setup your development version:

 - Fork or download the source code
 - Open chrome and click on the hamburger menu on the right hand corner 
 - Go to 'More tools' > ‘Extensions’
 - On the top right hand side tick ‘Developer mode’
 - Click ‘Load unpacked extension’
 - Select the ‘browser’ folder inside the original file ‘chrome-extension’
 - You will see the extension already enabled in your list of chrome extension

**Note:** If you tick ‘allow on incognito mode’ than you will always need to enable the extension on the incognito window.

#### 2. Install the host

 - Go to chrome-extension/host
 - Double click: install_host.sh


Usage
-----

The extension will inject NinjaPrinter.js into your website which containes the NinjaPrinter JavaScript object.

The NinjaPrinter object defines:
 -  3 public methods for printing:
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

**Note:** You can always read the source code, but if you want to test the example application, than you'll need to use
some webserver to make the example page available as `localhost` or `127.0.0.1`. Otherwise the content script will not
be injected. For example with php it's just `php -S localhost:8080`.

Development (OSX)
-----------------

### Extension

The chrome extension is standard Chrome extension, therefore all required documentation should be accessable in the [official
documentation](https://developer.chrome.com/extensions/getstarted). The only part which is somewhat less usual is the
[Native Messaging](https://developer.chrome.com/extensions/nativeMessaging).

### Host

#### Requirements

The Java host requires Java 8 to be installed, both for running the app (JRE) and for development (JDK).

#### Compiling the host application

You can setup Eclipse or Netbeans to build the project, but the easiest way is instaling [ant](http://ant.apache.org/)
 (*brew install ant*), and running *./chrome-extension/host/compile.sh* or opening the *java* directory and running ant.

#### Running the host application

```
java -jar ./chrome-extension/host/NinjaPrinter.jar
```

