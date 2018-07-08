package com.sdsu.edu.main.controller.db;

import com.sdsu.edu.main.view.ChartViewController;
import com.sdsu.edu.main.controller.ChartController;
import java.awt.Color;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.function.PolynomialFunction2D;
import org.jfree.data.function.PowerFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/*
 * Classes that support DBFReading for charttool
 */
public class DbfReadController {

  /*
   * Supporting classes for DBF read
   */
  public class DBFException extends IOException {

    public DBFException() {
      super();
    }

    public DBFException(String msg) {
      super(msg);
    }
  }

  class DBFHeader {

    static final byte SIG_DBASE_III = (byte) 0x03;
    /* DBF structure start here */
    byte signature; /* 0 */
    byte year; /* 1 */
    byte month; /* 2 */
    byte day; /* 3 */
    int numberOfRecords; /* 4-7 */
    short headerLength; /* 8-9 */
    short recordLength; /* 10-11 */
    short reserv1; /* 12-13 */
    byte incompleteTransaction; /* 14 */
    byte encryptionFlag; /* 15 */
    int freeRecordThread; /* 16-19 */
    int reserv2; /* 20-23 */
    int reserv3; /* 24-27 */
    byte mdxFlag; /* 28 */
    byte languageDriver; /* 29 */
    short reserv4; /* 30-31 */
    DBFField[] fieldArray; /* each 32 bytes */
    byte terminator1; /* n+1 */

    // byte[] databaseContainer; /* 263 bytes */
    /* DBF structure ends here */
    DBFHeader() {
      this.signature = SIG_DBASE_III;
      this.terminator1 = 0x0D;
    }

    void read(DataInput dataInput) throws IOException {
      signature = dataInput.readByte(); /* 0 */
      year = dataInput.readByte(); /* 1 */
      month = dataInput.readByte(); /* 2 */
      day = dataInput.readByte(); /* 3 */
      numberOfRecords = DbfUtils.readLittleEndianInt(dataInput); /* 4-7 */
      headerLength = DbfUtils.readLittleEndianShort(dataInput); /* 8-9 */
      recordLength = DbfUtils.readLittleEndianShort(dataInput); /* 10-11 */
      reserv1 = DbfUtils.readLittleEndianShort(dataInput); /* 12-13 */
      incompleteTransaction = dataInput.readByte(); /* 14 */
      encryptionFlag = dataInput.readByte(); /* 15 */
      freeRecordThread = DbfUtils.readLittleEndianInt(dataInput); /* 16-19 */
      reserv2 = dataInput.readInt(); /* 20-23 */
      reserv3 = dataInput.readInt(); /* 24-27 */
      mdxFlag = dataInput.readByte(); /* 28 */
      languageDriver = dataInput.readByte(); /* 29 */
      reserv4 = DbfUtils.readLittleEndianShort(dataInput); /* 30-31 */
      Vector v_fields = new Vector();
      DBFField field = DBFField.createField(dataInput); /* 32 each */
      while (field != null) {
        v_fields.addElement(field);
        field = DBFField.createField(dataInput);
      }
      fieldArray = new DBFField[v_fields.size()];
      for (int i = 0; i < fieldArray.length; i++) {
        fieldArray[i] = (DBFField) v_fields.elementAt(i);
      }
    }

    void write(DataOutput dataOutput) throws IOException {
      dataOutput.writeByte(signature); /* 0 */
      GregorianCalendar calendar = new GregorianCalendar();
      year = (byte) (calendar.get(Calendar.YEAR) - 1900);
      month = (byte) (calendar.get(Calendar.MONTH) + 1);
      day = (byte) (calendar.get(Calendar.DAY_OF_MONTH));
      dataOutput.writeByte(year); /* 1 */
      dataOutput.writeByte(month); /* 2 */
      dataOutput.writeByte(day); /* 3 */
      numberOfRecords = DbfUtils.littleEndian(numberOfRecords);
      dataOutput.writeInt(numberOfRecords); /* 4-7 */
      headerLength = findHeaderLength();
      dataOutput.writeShort(DbfUtils.littleEndian(headerLength)); /* 8-9 */
      recordLength = findRecordLength();
      dataOutput.writeShort(DbfUtils.littleEndian(recordLength)); /* 10-11 */
      dataOutput.writeShort(DbfUtils.littleEndian(reserv1)); /* 12-13 */
      dataOutput.writeByte(incompleteTransaction); /* 14 */
      dataOutput.writeByte(encryptionFlag); /* 15 */
      dataOutput.writeInt(DbfUtils.littleEndian(freeRecordThread));/* 16-19 */
      dataOutput.writeInt(DbfUtils.littleEndian(reserv2)); /* 20-23 */
      dataOutput.writeInt(DbfUtils.littleEndian(reserv3)); /* 24-27 */
      dataOutput.writeByte(mdxFlag); /* 28 */
      dataOutput.writeByte(languageDriver); /* 29 */
      dataOutput.writeShort(DbfUtils.littleEndian(reserv4)); /* 30-31 */
      for (int i = 0; i < fieldArray.length; i++) {
        fieldArray[i].write(dataOutput);
      }
      dataOutput.writeByte(terminator1); /* n+1 */
    }

    private short findHeaderLength() {
      return (short) (1 + 3 + 4 + 2 + 2 + 2 + 1 + 1 + 4 + 4 + 4 + 1 + 1
          + 2 + (32 * fieldArray.length) + 1);
    }

    private short findRecordLength() {
      int recordLength = 0;
      for (int i = 0; i < fieldArray.length; i++) {
        recordLength += fieldArray[i].getFieldLength();
      }
      return (short) (recordLength + 1);
    }
  } // end DbfHeader

  public static class DBFField {

    public static final byte FIELD_TYPE_C = (byte) 'C';
    public static final byte FIELD_TYPE_L = (byte) 'L';
    public static final byte FIELD_TYPE_N = (byte) 'N';
    public static final byte FIELD_TYPE_F = (byte) 'F';
    public static final byte FIELD_TYPE_D = (byte) 'D';
    public static final byte FIELD_TYPE_M = (byte) 'M';
    /* Field struct variables start here */
    byte[] fieldName = new byte[11]; /* 0-10 */
    byte dataType; /* 11 */
    int reserv1; /* 12-15 */
    int fieldLength; /* 16 */
    byte decimalCount; /* 17 */
    short reserv2; /* 18-19 */
    byte workAreaId; /* 20 */
    short reserv3; /* 21-22 */
    byte setFieldsFlag; /* 23 */
    byte[] reserv4 = new byte[7]; /* 24-30 */
    byte indexFieldFlag; /* 31 */
    /* Field struct variables end here */
    /* other class variables */
    int nameNullIndex = 0;

    public static DBFField createField(DataInput in) throws IOException {
      DBFField field = new DBFField();
      byte t_byte = in.readByte(); /* 0 */
      if (t_byte == (byte) 0x0d) {
        return null;
      }
      in.readFully(field.fieldName, 1, 10); /* 1-10 */
      field.fieldName[0] = t_byte;
      for (int i = 0; i < field.fieldName.length; i++) {
        if (field.fieldName[i] == (byte) 0) {
          field.nameNullIndex = i;
          break;
        }
      }
      field.dataType = in.readByte(); /* 11 */
      field.reserv1 = DbfUtils.readLittleEndianInt(in); /* 12-15 */
      field.fieldLength = in.readUnsignedByte(); /* 16 */
      field.decimalCount = in.readByte(); /* 17 */
      field.reserv2 = DbfUtils.readLittleEndianShort(in); /* 18-19 */
      field.workAreaId = in.readByte(); /* 20 */
      field.reserv2 = DbfUtils.readLittleEndianShort(in); /* 21-22 */
      field.setFieldsFlag = in.readByte(); /* 23 */
      in.readFully(field.reserv4); /* 24-30 */
      field.indexFieldFlag = in.readByte(); /* 31 */
      return field;
    }

    protected void write(DataOutput out) throws IOException {
      // Field Name
      out.write(fieldName); /* 0-10 */
      out.write(new byte[11 - fieldName.length]);
      // data type
      out.writeByte(dataType); /* 11 */
      out.writeInt(0x00); /* 12-15 */
      out.writeByte(fieldLength); /* 16 */
      out.writeByte(decimalCount); /* 17 */
      out.writeShort((short) 0x00); /* 18-19 */
      out.writeByte((byte) 0x00); /* 20 */
      out.writeShort((short) 0x00); /* 21-22 */
      out.writeByte((byte) 0x00); /* 23 */
      out.write(new byte[7]); /* 24-30 */
      out.writeByte((byte) 0x00); /* 31 */
    }

    public String getName() {
      return new String(this.fieldName, 0, nameNullIndex);
    }

    public byte getDataType() {
      return dataType;
    }

    public int getFieldLength() {
      return fieldLength;
    }

    public int getDecimalCount() {
      return decimalCount;
    }

    public void setDataType(byte value) {
      switch (value) {
        case 'D':
          this.fieldLength = 8; /* fall through */
        case 'C':
        case 'L':
        case 'N':
        case 'F':
        case 'M':
          this.dataType = value;
          break;
        default:
          throw new IllegalArgumentException("Unknown data type");
      }
    }
  } // end DbfField

  public final static class DbfUtils {

    public static final int ALIGN_LEFT = 10;
    public static final int ALIGN_RIGHT = 12;

    private DbfUtils() {
    }

    public static int readLittleEndianInt(DataInput in) throws IOException {
      int bigEndian = 0;
      for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
        bigEndian |= (in.readUnsignedByte() & 0xff) << shiftBy;
      }
      return bigEndian;
    }

    public static short readLittleEndianShort(DataInput in) throws IOException {
      int low = in.readUnsignedByte() & 0xff;
      int high = in.readUnsignedByte();
      return (short) (high << 8 | low);
    }

    public static byte[] trimLeftSpaces(byte[] arr) {
      StringBuffer t_sb = new StringBuffer(arr.length);
      for (int i = 0; i < arr.length; i++) {
        if (arr[i] != ' ') {
          t_sb.append((char) arr[i]);
        }
      }
      return t_sb.toString().getBytes();
    }

    public static short littleEndian(short value) {
      short num1 = value;
      short mask = (short) 0xff;
      short num2 = (short) (num1 & mask);
      num2 <<= 8;
      mask <<= 8;
      num2 |= (num1 & mask) >> 8;
      return num2;
    }

    public static int littleEndian(int value) {
      int num1 = value;
      int mask = 0xff;
      int num2 = 0x00;
      num2 |= num1 & mask;
      for (int i = 1; i < 4; i++) {
        num2 <<= 8;
        mask <<= 8;
        num2 |= (num1 & mask) >> (8 * i);
      }
      return num2;
    }

    public static byte[] textPadding(String text, String characterSetName,
        int length) throws java.io.UnsupportedEncodingException {
      return textPadding(text, characterSetName, length,
          DbfUtils.ALIGN_LEFT);
    }

    public static byte[] textPadding(String text, String characterSetName,
        int length, int alignment) throws java.io.UnsupportedEncodingException {
      return textPadding(text, characterSetName, length, alignment, (byte) ' ');
    }

    public static byte[] textPadding(String text, String characterSetName,
        int length, int alignment, byte paddingByte)
        throws java.io.UnsupportedEncodingException {
      if (text.length() >= length) {
        return text.substring(0, length).getBytes(characterSetName);
      }
      byte byte_array[] = new byte[length];
      Arrays.fill(byte_array, paddingByte);
      switch (alignment) {
        case ALIGN_LEFT:
          System.arraycopy(text.getBytes(characterSetName), 0,
              byte_array, 0, text.length());
          break;
        case ALIGN_RIGHT:
          int t_offset = length - text.length();
          System.arraycopy(text.getBytes(characterSetName), 0,
              byte_array, t_offset, text.length());
          break;
      }
      return byte_array;
    }

    public static byte[] doubleFormating(Double doubleNum,
        String characterSetName, int fieldLength, int sizeDecimalPart)
        throws java.io.UnsupportedEncodingException {
      int sizeWholePart = fieldLength - (sizeDecimalPart > 0 ? (sizeDecimalPart + 1) : 0);
      StringBuffer format = new StringBuffer(fieldLength);
      for (int i = 0; i < sizeWholePart; i++) {
        format.append("#");
      }
      if (sizeDecimalPart > 0) {
        format.append(".");
        for (int i = 0; i < sizeDecimalPart; i++) {
          format.append("0");
        }
      }
      DecimalFormat df = new DecimalFormat(format.toString());
      return textPadding(df.format(doubleNum.doubleValue()).toString(),
          characterSetName, fieldLength, ALIGN_RIGHT);
    }

    public static boolean contains(byte[] arr, byte value) {
      boolean found = false;
      for (int i = 0; i < arr.length; i++) {
        if (arr[i] == value) {
          found = true;
          break;
        }
      }
      return found;
    }
  } // end DbfUtils

  public class DBFReader {

    protected String characterSetName = "8859_1";
    protected final int END_OF_DATA = 0x1A;
    DataInputStream dataInputStream;
    DBFHeader header;
    /* Class specific variables */
    boolean isClosed = true;

    public DBFReader(InputStream in) throws DBFException {
      try {
        this.dataInputStream = new DataInputStream(in);
        this.isClosed = false;
        this.header = new DBFHeader();
        this.header.read(this.dataInputStream);
        /* it might be required to leap to the start of records at times */
        int t_dataStartIndex = this.header.headerLength
            - (32 + (32 * this.header.fieldArray.length)) - 1;
        if (t_dataStartIndex > 0) {
          dataInputStream.skip(t_dataStartIndex);
        }
      } catch (IOException e) {
        throw new DBFException(e.getMessage());
      }
    }

    public String toString() {
      StringBuffer sb = new StringBuffer(this.header.year + "/"
          + this.header.month + "/" + this.header.day + "\n"
          + "Total records: " + this.header.numberOfRecords
          + "\nHEader length: " + this.header.headerLength + "");
      for (int i = 0; i < this.header.fieldArray.length; i++) {
        sb.append(this.header.fieldArray[i].getName());
        sb.append("\n");
      }
      return sb.toString();
    }

    public int getRecordCount() {
      return this.header.numberOfRecords;
    }

    public DBFField getField(int index) throws DBFException {
      if (isClosed) {
        throw new DBFException("Source is not open");
      }
      return this.header.fieldArray[index];
    }

    public int getFieldCount() throws DBFException {
      if (isClosed) {
        throw new DBFException("Source is not open");
      }
      if (this.header.fieldArray != null) {
        return this.header.fieldArray.length;
      }
      return -1;
    }

    public Object[] nextRecord() throws DBFException {
      if (isClosed) {
        throw new DBFException("Source is not open");
      }
      Object recordObjects[] = new Object[this.header.fieldArray.length];
      try {
        boolean isDeleted = false;
        do {
          if (isDeleted) {
            dataInputStream.skip(this.header.recordLength - 1);
          }
          int t_byte = dataInputStream.readByte();
          if (t_byte == END_OF_DATA) {
            return null;
          }
          isDeleted = (t_byte == '*');
        } while (isDeleted);
        for (int i = 0; i < this.header.fieldArray.length; i++) {
          switch (this.header.fieldArray[i].getDataType()) {
            case 'C':
              byte b_array[] = new byte[this.header.fieldArray[i]
                  .getFieldLength()];
              dataInputStream.read(b_array);
              recordObjects[i] = new String(b_array, characterSetName);
              break;
            case 'D':
              byte t_byte_year[] = new byte[4];
              dataInputStream.read(t_byte_year);
              byte t_byte_month[] = new byte[2];
              dataInputStream.read(t_byte_month);
              byte t_byte_day[] = new byte[2];
              dataInputStream.read(t_byte_day);
              try {
                GregorianCalendar calendar = new GregorianCalendar(
                    Integer.parseInt(new String(t_byte_year)),
                    Integer.parseInt(new String(t_byte_month)) - 1,
                    Integer.parseInt(new String(t_byte_day)));
                recordObjects[i] = calendar.getTime();
              } catch (NumberFormatException e) {
                /*
                 * this field may be empty or may have improper
                 * value set
                 */
                recordObjects[i] = null;
              }
              break;
            case 'F':
              try {
                byte t_float[] = new byte[this.header.fieldArray[i].getFieldLength()];
                dataInputStream.read(t_float);
                t_float = DbfUtils.trimLeftSpaces(t_float);
                if (t_float.length > 0 && !DbfUtils.contains(t_float, (byte) '?')) {
                  recordObjects[i] = new Float(new String(t_float));
                } else {
                  recordObjects[i] = null;
                }
              } catch (NumberFormatException e) {
                throw new DBFException("Failed to parse Float: " + e.getMessage());
              }
              break;
            case 'N':
              try {
                byte t_numeric[] = new byte[this.header.fieldArray[i].getFieldLength()];
                dataInputStream.read(t_numeric);
                t_numeric = DbfUtils.trimLeftSpaces(t_numeric);
                if (t_numeric.length > 0 && !DbfUtils.contains(t_numeric, (byte) '?')) {
                  recordObjects[i] = new Double(new String(t_numeric));
                } else {
                  recordObjects[i] = null;
                }
              } catch (NumberFormatException e) {
                throw new DBFException("Failed to parse Number: " + e.getMessage());
              }
              break;
            case 'L':
              byte t_logical = dataInputStream.readByte();
              if (t_logical == 'Y' || t_logical == 't'
                  || t_logical == 'T' || t_logical == 't') {
                recordObjects[i] = Boolean.TRUE;
              } else {
                recordObjects[i] = Boolean.FALSE;
              }
              break;
            default:
              recordObjects[i] = new String("null");
          }
        }
      } catch (EOFException e) {
        return null;
      } catch (IOException e) {
        throw new DBFException(e.getMessage());
      }
      return recordObjects;
    }
  } // end DbfReader

  public class CharacterRecord {

    public HashMap<String, List<String>> fieldAndValues;

    public CharacterRecord() {
      fieldAndValues = new HashMap<String, List<String>>();
    }
  }

  public class NumericRecord {

    public HashMap<String, List<Double>> fieldAndValues;

    public NumericRecord() {
      fieldAndValues = new HashMap<String, List<Double>>();
    }
  }

  /*
   * Data Table:  rows and coloumns are read
   */
  InputStream inputStream = null;
  DBFReader dbfReader = null;
  List<DBFField> charList = new ArrayList<DBFField>();
  List<DBFField> numericFullList = new ArrayList<DBFField>();
  List<DBFField> doubleList = new ArrayList<DBFField>();
  List<DBFField> logicalList = new ArrayList<DBFField>();
  List<DBFField> dataList = new ArrayList<DBFField>();
  List<String> charNameList = new ArrayList<String>();
  List<String> stateNameList = new ArrayList<String>();
  List<String> numericList = new ArrayList<String>();
  List<String> doubleNameList = new ArrayList<String>();
  List<String> logicalNameList = new ArrayList<String>();
  List<String> dataNameList = new ArrayList<String>();
  List<List<Double>> temporaryNumericValues = new ArrayList<List<Double>>();
  List<String> fieldNameList = new ArrayList<String>();
  HashMap<String, Integer> numericMap = new HashMap<String, Integer>();
  NumericRecord numericRecord = new NumericRecord();
  HashMap<String, Integer> charMap = new HashMap<String, Integer>();
  CharacterRecord charRecord = new CharacterRecord();
  private static DbfReadController DbfRead = new DbfReadController();

  /* Static 'instance' method */
  public static DbfReadController getInstance() {
    return DbfRead;
  }

  public List<String> readnumericdbf(File dbffile) {
    try {
      // create a DBFReader object
      inputStream = new FileInputStream(dbffile);
      dbfReader = new DBFReader(inputStream);
      // get the field count
      int numberOfFields = dbfReader.getFieldCount();
      // fetch all the field information
      for (int i = 0; i < numberOfFields; i++) {
        DBFField field = dbfReader.getField(i);
        /*
         * XBase Type XBase Symbol Java Type used in JavaDBF Character C
         * java.lang.String Numeric N java.lang.Double Double F
         * lava.lang.Double Logical L java.lang.Boolean Date D
         * java.util.Date
         */
        if (field.getDataType() == DBFField.FIELD_TYPE_N) {
          numericList.add(field.getName());
          numericFullList.add(field);
          numericMap.put(field.getName(), i);
        }
      }
      // reading the rows
      Object[] rowObjects;
      while ((rowObjects = dbfReader.nextRecord()) != null) {
        // reading the rows for numeric types
        for (String fieldName : numericMap.keySet()) {
          if (!(numericRecord.fieldAndValues.containsKey(fieldName))) {
            numericRecord.fieldAndValues.put(fieldName, new ArrayList<Double>());
          }
          List<Double> recordvalues = numericRecord.fieldAndValues.get(fieldName);
          recordvalues.add((Double) rowObjects[numericMap.get(fieldName)]);
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (DBFException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return (List<String>) numericList;
  }

  public List<String> readchardbf(File dbffile) {
    try {
      // create a DBFReader object
      inputStream = new FileInputStream(dbffile);
      dbfReader = new DBFReader(inputStream);
      // get the field count
      int numberOfFields = dbfReader.getFieldCount();
      // fetch all the field information
      for (int i = 0; i < numberOfFields; i++) {
        DBFField field = dbfReader.getField(i);
        /*
         * XBase Type XBase Symbol Java Type used in JavaDBF Character C
         * java.lang.String Numeric N java.lang.Double Double F
         * lava.lang.Double Logical L java.lang.Boolean Date D
         * java.util.Date
         */
        if (field.getDataType() == DBFField.FIELD_TYPE_C) {
          charList.add(field);
          charNameList.add(field.getName());
          charMap.put(field.getName(), i);
        }
      }
      // reading the rows
      Object[] rowObjects;
      while ((rowObjects = dbfReader.nextRecord()) != null) {
        // reading the rows for character types
        for (String fieldName : charMap.keySet()) {
          if (!(charRecord.fieldAndValues.containsKey(fieldName))) {
            charRecord.fieldAndValues.put(fieldName, new ArrayList<String>());
          }
          List<String> recordcharvalues = charRecord.fieldAndValues.get(fieldName);
          recordcharvalues.add((String) rowObjects[charMap.get(fieldName)]);
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (DBFException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return (List<String>) charNameList;
  }

  public CharacterRecord getCharRecord() {
    return charRecord;
  }

  public NumericRecord getNumericRecord() {
    return numericRecord;
  }

  public String dataHandler(List<String> selectedFields, String chartSType,
      String characterNameSType, String chartColorSType) {
    String[] stateName;
    if (chartSType.equalsIgnoreCase("Pie")) {
      if (selectedFields.size() > 1) {
        final JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, "Retry. Select not more than 1 fields.");
        frame.dispose();
        System.exit(0);
      }
    } else if (selectedFields.size() > 2) {
      final JFrame frame = new JFrame();
      JOptionPane.showMessageDialog(frame, "Retry. Select not more than 2 fields.");
      frame.dispose();
      System.exit(0);
    }
    // reading the rows for character types
    // for character
    for (String fieldName : (charRecord).fieldAndValues.keySet()) {
      if (fieldName.equals(characterNameSType)) {
        stateNameList.addAll((charRecord).fieldAndValues.get(fieldName));
      }
    }
    stateName = stateNameList.toArray(new String[stateNameList.size()]);
    ChartController chart = ChartController.getInstance();

    for (String fieldName : (numericRecord).fieldAndValues.keySet()) {
      for (String selectedName : selectedFields) {
        if (fieldName.equals(selectedName)) {
          // pie
          if (chartSType.equalsIgnoreCase("Pie")) {
            chart.pieChart(chartColorSType, fieldName, stateName,
                (numericRecord).fieldAndValues.get(fieldName));
          }
          // "Horizontal"
          if (chartSType.equalsIgnoreCase("Horizontal")) {
            if (selectedFields.size() == 1) {
              chart = new ChartController();
              chart.horizontalSingleBarChart(chartColorSType, fieldName, stateName,
                  (numericRecord).fieldAndValues.get(fieldName));
            } else if (selectedFields.size() == 2) {
              fieldNameList.add(fieldName);
              temporaryNumericValues.add((numericRecord).fieldAndValues.get(fieldName));
              if (temporaryNumericValues.size() > 1) {
                chart.horizontalMultiBarChart(chartColorSType, fieldNameList, stateName,
                    temporaryNumericValues);
              }
            }
          }
          // "Vertical"
          if (chartSType.equalsIgnoreCase("Vertical")) {
            if (selectedFields.size() == 1) {
              chart.verticalSingleBarChart(chartColorSType, fieldName, stateName,
                  (numericRecord).fieldAndValues.get(fieldName));
            } else if (selectedFields.size() == 2) {
              fieldNameList.add(fieldName);
              temporaryNumericValues.add((numericRecord).fieldAndValues.get(fieldName));
              if (temporaryNumericValues.size() > 1) {
                chart.verticalMultiBarChart(chartColorSType, fieldNameList, stateName,
                    temporaryNumericValues);
              }
            }
          }
        }
      } // end for
    }  // end for
    return null;
  } // end dataHandler
}
