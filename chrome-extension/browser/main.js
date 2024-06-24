var Main = {

    hostName: "de.westwing.chrome.printer.ninja",

    init: function () {
        // Global message lister, that routes messages to methods.
        chrome.runtime.onMessage.addListener(
            function (request, sender, sendResponse) {
                console.info("Main.listener: ", request);

                if (request.fn && request.fn in Main) {
                    Main[request.fn](request, sender, sendResponse);
                } else {
                    Main.print(request, sender, sendResponse);
                }
            }
        );
    },

    print: function (request, sender, sendResponse) {
        console.info('Main.print');
        console.info("Sender", sender);
        console.info("Request", request);
        console.info("sendResponse", sendResponse);

        chrome.runtime.sendNativeMessage(Main.hostName, request, function (response) {
            if (!response) {
                response = {success: false, message: chrome.runtime.lastError.message}
            }
            console.info("Response: ", response);

            // Send response back to content script
            chrome.tabs.sendMessage(sender.tab.id, {action: "print", response: response})
            .then((response) => {
                console.info("tabs.sendMessage Response", response);
            })
            .catch((error) => {
                console.error("tabs.sendMessage Error", error);
            });
        });

        console.info("Main.print: done.");
    },

    log: function (request, sender, sendResponse) {
        console.info(request.message || '');
    }
};

Main.init();
