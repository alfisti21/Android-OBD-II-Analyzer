package jellodev.obd2analyzerv10.obd.commands;

import jellodev.obd2analyzerv10.obd.exceptions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.lang.Class;
import java.lang.String;


public abstract class ObdCommand {


    private final Class[] ERROR_CLASSES = {
            UnableToConnectException.class,
            BusInitException.class,
            MisunderstoodCommandException.class,
            NoDataException.class,
            StoppedException.class,
            UnknownErrorException.class,
            UnsupportedCommandException.class
    };
    protected ArrayList<Integer> buffer = null;
    protected String cmd = null;
    protected boolean useImperialUnits = false;
    protected String rawData = null;
    private Long responseDelayInMs = null;
    private long start;
    private long end;


    public ObdCommand(String command) {
        this.cmd = command;
        this.buffer = new ArrayList<>();
    }


    public ObdCommand(ObdCommand other) {
        this(other.cmd);
    }


    public void run(InputStream in, OutputStream out) throws IOException,
            InterruptedException {
        start = System.currentTimeMillis();
        sendCommand(out);
        readResult(in);
        end = System.currentTimeMillis();
        synchronized (ObdCommand.class) { //Only one command can write and read a data in one time.
            start = System.currentTimeMillis();
            sendCommand(out);
            readResult(in);
            end = System.currentTimeMillis();
        }
    }


    private void sendCommand(OutputStream out) throws IOException,
            InterruptedException {
        // write to OutputStream (i.e.: a BluetoothSocket) with an added
        // Carriage return
        out.write((cmd +="\r").getBytes());
        out.flush();
        if (responseDelayInMs != null && responseDelayInMs > 0) {
            Thread.sleep(responseDelayInMs);
        }
    }


    protected void resendCommand(OutputStream out) throws IOException,
            InterruptedException {
        out.write("\r".getBytes());
        out.flush();
        if (responseDelayInMs != null && responseDelayInMs > 0) {
            Thread.sleep(responseDelayInMs);
        }
    }


    protected void readResult(InputStream in) throws IOException {
        readRawData(in);
        checkForErrors();
        fillBuffer();
        performCalculations();
    }

    protected abstract void performCalculations();

    protected void fillBuffer() {
        rawData = rawData.replaceAll("\\s", ""); //removes all [ \t\n\x0B\f\r]
        rawData = rawData.replaceAll("(BUS INIT)|(BUSINIT)|(\\.)", "");

        if (!rawData.matches("([0-9A-F])+")) {
            throw new NonNumericResponseException(rawData);
        }

        // read string each two chars
        buffer.clear();
        int begin = 0;
        int end = 2;
        while (end <= rawData.length()) {
            buffer.add(Integer.decode("0x" + rawData.substring(begin, end)));
            begin = end;
            end += 2;
        }
    }

    protected void readRawData(InputStream in) throws IOException {
        byte b;
        StringBuilder res = new StringBuilder();

        // read until '>' arrives OR end of stream reached
        char c;
        // -1 if the end of the stream is reached
        while (((b = (byte) in.read()) > -1)) {
            c = (char) b;
            if (c == '>') // read until '>' arrives
            {
                break;
            }
            res.append(c);
        }

        rawData = res.toString().replaceAll("SEARCHING", "");



        rawData = rawData.replaceAll("\\s", "");
    }

    private void checkForErrors() {
        for (@SuppressWarnings("unchecked") Class<? extends ResponseException> errorClass : ERROR_CLASSES) {
            ResponseException messageError;

            try {
                messageError = errorClass.newInstance();
                messageError.setCommand(this.cmd);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (messageError.isError(rawData)) {
                throw messageError;
            }
        }
    }

    public String getResult() {
        return rawData;
    }

    public abstract String getFormattedResult();

    public abstract String getCalculatedResult();

    protected ArrayList<Integer> getBuffer() {
        return buffer;
    }

    public boolean useImperialUnits() {
        return useImperialUnits;
    }

    public String getResultUnit() {
        return "";//no unit by default
    }

    public void useImperialUnits(boolean isImperial) {
        this.useImperialUnits = isImperial;
    }

    public abstract String getName();

    public Long getResponseTimeDelay() {
        return responseDelayInMs;
    }

    public void setResponseTimeDelay(Long responseDelayInMs) {
        this.responseDelayInMs = responseDelayInMs;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    protected final String getCommandPID() {
        return cmd.substring(3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObdCommand that = (ObdCommand) o;

        return cmd != null ? cmd.equals(that.cmd) : that.cmd == null;
    }

    @Override
    public int hashCode() {
        return cmd != null ? cmd.hashCode() : 0;
    }

}
