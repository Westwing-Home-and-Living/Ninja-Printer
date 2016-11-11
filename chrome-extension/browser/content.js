/**
* Class representing a Semantic Versioning object.
*/
class SemVer {
        /**
        * Parses a version string and populates the object.
        * Important: Your tag names need to follow the Semantic Version standard
        * or else this will not work.
        *
        * @param {string} version The output of the `git describe` command
        */
        constructor(version) {
            /**
            * A pattern representing the `git describe` command output
            * - The first `(\d+)` matches the major version
            * - The second `(\d+)` matches the minor version
            * - The third `(\d+)` matches the patch version
            * - `\-?\d*\-?g?` (optional) matches the number of commits after
            *   the tag and the git vcs identifier
            * - `([0-9a-f]*)` (optional) matches the abbreviated object name
            *   of the commit of HEAD
            */
            let semVerRegex = /(\d+)\.(\d+)\.(\d+)\-?\d*\-?g?([0-9a-f]*)/;

            let result = version.match(semVerRegex);

            if (result === null) {
                this.major  = null;
                this.minor  = null;
                this.patch  = null;
                this.commit = null;
            }

            this.major  = parseInt(result[1]);
            this.minor  = parseInt(result[2]);
            this.patch  = parseInt(result[3]);
            this.commit = parseInt(result[4]);
        }

        /**
        * @return {string}
        */
        get Major() {
            return this.major;
        }

        /**
        * @return {string}
        */
        get Minor() {
            return this.minor;
        }

        /**
        * @return {string}
        */
        get Patch() {
            return this.patch;
        }

        /**
        * @return {string}
        */
        get Commit() {
            return this.commit || '';
        }

        /**
        * Compares this version to another, returns:
        * - < 0 if it's less than other
        * - 0 if it's the same as other
        * - > 0 if it's larger than other
        *
        * Note: Only the major, minor and patch values are considered.
        *
        * @param {SemVer} other
        */
        compare(other) {
            let majorDifference = this.Major - other.Major;
            if (majorDifference !== 0) {
                return majorDifference;
            }

            let minorDifference = this.Minor - other.Minor;
            if (minorDifference !== 0) {
                return minorDifference;
            }

            let patchDifference = this.Patch - other.Patch;
            if (patchDifference !==0) {
                return patchDifference;
            }

            return 0;
        }
    }

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

    // Listen for response messages from background script
    _initBackgroundListener: function () {
        let that = this;
        chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {
            console.info("NinjaPrinterCore.response", request);

            if (request.action === 'print') {
                let manifest = chrome.runtime.getManifest();
                that._compareVersions(manifest.version_name, request.response.version);

                let customEvent = new CustomEvent("ninjaprinter.result", {detail: request.response});
                document.dispatchEvent(customEvent);
            }
        });
    },

    /** 
    * Compare the extention and host versions. Emit a "ninjaprinter.versionWarning" if they don't match.
    * Also warn when either of the versions contain a commit sha1 indicating that it's a dev version.
    *
    * @param {String} extension The extension version.
    * @param {String} host      The host version.
    */
    _compareVersions: function(extension, host) {
        let hostVersion = new SemVer(host);

        let extensionVersion = new SemVer(extension);

        let difference = extensionVersion.compare(hostVersion);

        if (difference !== 0) {
            let message = `The NinjaPrinter extension and host versions do not match.\n` +
                          `The extension version is ${difference < 0 ? 'lower' : 'higher'} than the host.`;

            let customEvent = new CustomEvent("ninjaprinter.versionWarning", {
                detail: {
                    message: message,
                    extensionVersion: extension,
                    hostVersion: host
                }
            });
            document.dispatchEvent(customEvent);
        }

        if (hostVersion.Commit !== '') {
            let message = 'You are using a development version of the NinjaPrinter host.';

            let customEvent = new CustomEvent("ninjaprinter.versionWarning", {
                detail: {
                    message: message,
                    extensionVersion: extension,
                    hostVersion: host
                }
            });
            document.dispatchEvent(customEvent);
        }

        if (extensionVersion.Commit !== '') {
            let message = 'You are using a development version of the NinjaPrinter extension.';

            let customEvent = new CustomEvent("ninjaprinter.versionWarning", {
                detail: {
                    message: message,
                    extensionVersion: extension,
                    hostVersion: host
                }
            });
            document.dispatchEvent(customEvent);
        }
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
