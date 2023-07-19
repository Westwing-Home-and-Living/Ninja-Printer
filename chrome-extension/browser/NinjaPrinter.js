// NinjaPrinter gets injected into the web pages that match the content_scripts in the manifest.json
var NinjaPrinter = {
    // Supported custom events
    events: {
        beforePrint: "ninjaprinter.beforePrint",
        afterPrint: "ninjaprinter.afterPrint",
        result: "ninjaprinter.result"
    },

    //  Attached cutom events listeners map
    listeners: {
        "ninjaprinter.result": [],
        "ninjaprinter.afterPrint": [],
        "ninjaprinter.beforePrint": []
    },

    // Init Object.
    init: function () {
        console.info("NinjaPrinter.init()");
        this._initListeners();
    },

    // Triggers a print request
    // var request = {
    //     printerName: "printerName",
    //     printerType: "[label|PDF]",
    //     fileContent: "base64encoded_or_absolutePath",
    //     requestId: "requestId",
    //     secondaryPrinterName: "secondaryPrinterName",
    // }
    print: function (request) {
        this._fireEvent(this.events.beforePrint, {data: request});
        window.postMessage(request, "*");
        this._fireEvent(this.events.afterPrint, {data: request});
    },

    // Triggers printing a zpl document
    printZpl: function (printerName, base64Content, requestId, secondaryPrinterName) {
        this.print({printerName: printerName, fileContent: base64Content, printerType: "label", requestId: requestId, secondaryPrinterName: secondaryPrinterName});
    },

    // Triggers printing a pdf document
    printPdf: function (printerName, base64Content, requestId, secondaryPrinterName) {
        this.print({printerName: printerName, fileContent: base64Content, printerType: "PDF", requestId: requestId, secondaryPrinterName: secondaryPrinterName});
    },

    // @deprecated
    printLabel: function (printerName, base64Content, requestId, secondaryPrinterName) {
        this.printZpl(printerName, base64Content, requestId, secondaryPrinterName);
    },

    // @deprecated
    printPDF: function (printerName, base64Content, requestId, secondaryPrinterName) {
        this.printPdf(printerName, base64Content, requestId, secondaryPrinterName);
    },

    // Attach an event listener.
    on: function (eventName, callback) {
        if (NinjaPrinter.listeners[eventName]) {
            NinjaPrinter.listeners[eventName].push(callback);
        }

        return NinjaPrinter;
    },

    // Remove event listeners from a specific event
    off: function (eventName) {
        if (NinjaPrinter.listeners[eventName]) {
            NinjaPrinter.listeners[eventName] = [];
        }

        return NinjaPrinter;
    },

    // Helper method to create and dispatch custom events.
    _fireEvent: function (eventName, data) {
        var customEvent = new CustomEvent(eventName, data);

        document.dispatchEvent(customEvent);
    },

    // Helper method to attach event listeners.
    _initListeners: function () {
        document.addEventListener("ninjaprinter.result", function (evt) {
            console.info("Event is: ", evt);
            NinjaPrinter._executeListeners("ninjaprinter.result", evt);
        });

        document.addEventListener("ninjaprinter.beforePrint", function (evt) {
            console.info("Event is: ", evt);
            NinjaPrinter._executeListeners("ninjaprinter.beforePrint", evt);
        });

        document.addEventListener("ninjaprinter.afterPrint", function (evt) {
            console.info("Event is: ", evt);
            NinjaPrinter._executeListeners("ninjaprinter.afterPrint", evt);
        });
    },

    // Helper method to trigger attached events'
    _executeListeners: function (eventName, evt) {
        if (!NinjaPrinter.listeners[eventName]) {
            return;
        }

        for (var i = 0; i < NinjaPrinter.listeners[eventName].length; i++) {
            console.info("Executing Callbacks: ", NinjaPrinter.listeners[eventName][i]);
            NinjaPrinter.listeners[eventName][i](evt.detail);
        }
    }
};

NinjaPrinter.init();
