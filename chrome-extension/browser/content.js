// NinjaPrinter Core sets up communication between NinjaPrinter and
var NinjaPrinterCore = {

    init: function () {
        console.info("NinjaPrinterCore.init()");
        this._initInjectedScriptListeners();
        this._initBackgroundListener();
        this._injectPageScripts();
    },

    print: function (request) {
        this._sendMessage(request, function (response) {
        });
    },

    // Logs messages to background console for easy debugging.
    log: function (msg) {
        this._sendMessage({fn: 'log', message: msg});
    },

    // Listen for response messages form background script
    _initBackgroundListener: function () {
        chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {
            console.info("NinjaPrinterCore.response", request);

            if (request.action === 'print') {
                customEvent = new CustomEvent("ninjaprinter.result", {detail: request.response});
                document.dispatchEvent(customEvent);
            }
        });
    },

    // Listen for messages from the injected script
    _initInjectedScriptListeners: function () {
        window.addEventListener("message", function (event) {
            console.info("Content script received: ", event);

            // We only accept messages from ourselves
            if (event.source != window) {
                return;
            }

            if (!NinjaPrinterCore._validateRequest(event.data)) {
                return console.info('Invalid request, make sure all parameters are set.');
            }

            NinjaPrinterCore.print(event.data);

        }, false);
    },

    // Inject web accessible scripts to the current page.
    _injectPageScripts: function () {
        var scriptElement = document.createElement('script');
        var parent = (document.head || document.body);

        scriptElement.src = chrome.extension.getURL('NinjaPrinter.js');

        parent.insertBefore(scriptElement, parent.firstChild);
    },

    // Sends messages to the backgorund script.
    _sendMessage: function (request, callback) {
        console.info("_sendMessage:", request);

        chrome.runtime.sendMessage(request, function (response) {
            console.info("_sendMessage Response: ", response);
        });

        console.info("_sendMessage end");
    },

    // Validates required request params
    _validateRequest: function (request) {
        if (!request.printerName || request.printerName == '') {
            return false;
        }

        if (!request.printerType) {
            return false;
        }

        if (request.printerType.toUpperCase() != 'LABEL' && request.printerType.toUpperCase() != 'PDF') {
            return false;
        }

        if (!request.fileContent || request.fileContent == '') {
            return false;
        }

        return true;
    }
};

NinjaPrinterCore.init();
