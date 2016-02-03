(function(){
    var outputContainer, zplPrinter, pdfPrinter, tries = 0, maxTries = 100, intervalId;

    function addRow(text){
        var time, html;

        time = (new Date()).toLocaleTimeString("en-US");

        html = '<tr><td>' + text + '</td><td>' + time + '</td></tr>';

        outputContainer.prepend(html);
    }

    function onBeforePrint(result){
        addRow('Printing is to be started');
    }

    function onAfterPrint(result){
        addRow('Printing has been started.');
    }

    function onResult(result){
        addRow('<samp>' + JSON.stringify(result) + '</samp>');
    }

    function submitZpl(e){
        var printerName;

        e.preventDefault();

        if (!ready) {
            return;
        }

        printerName = zplPrinter.val();

        if (printerName) {
            addRow('Zpl printing is to start on "' + printerName + '"');

            NinjaPrinter.printZpl(zplPrinter.val(), zpl64);
        } else {
            addRow('Zpl printing is skipped, printer name is missing.');
        }
    }

    function submitPdf(e){
        var printerName;

        e.preventDefault();

        if (!ready) {
            return;
        }

        printerName = pdfPrinter.val();

        if (printerName) {
            addRow('Pdf printing is to start on "' + printerName + '"');

            NinjaPrinter.printPdf(pdfPrinter.val(), pdf64);
        } else {
            addRow('Pdf printing is skipped, printer name is missing.');
        }
    }

    function isReady(){
        if (typeof NinjaPrinter == 'undefined') {
            tries += 1;

            if (tries > maxTries) {
                window.clearInterval(intervalId);
            }

            return;
        }

        window.clearInterval(intervalId);

        ready = true;

        NinjaPrinter.on('ninjaprinter.beforePrint', onBeforePrint);
        NinjaPrinter.on('ninjaprinter.afterPrint', onAfterPrint);
        NinjaPrinter.on('ninjaprinter.result', onResult);

        outputContainer = $('#output');

        zplPrinter = $('#zpl-printer');
        pdfPrinter = $('#pdf-printer');

        $('#zpl-form').submit(submitZpl);
        $('#pdf-form').submit(submitPdf);
    }

    $(document).ready(function(){
        intervalId = window.setInterval(isReady, 100);
    });
})();

