package com.mycompany.hotel_management_system;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.print.PrinterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.formdev.flatlaf.FlatDarkLaf;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultComboBoxModel;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.Timer;

/**
 *
 * @author Daniel
 */
public class Final_Frame extends javax.swing.JFrame {
    //Date Format
    SimpleDateFormat myDateFormat = new SimpleDateFormat("EEEEE MMMMM dd, yyyy");
    
    //Values for roomNumber JComboBox
    String[] singleRoom = {"101", "102", "103", "104", "105"};
    String[] doubleRoom = {"201", "202", "203", "204", "205"};
    String[] guestRoom = {"301", "302", "303", "304", "305"};
    String[] vipRoom = {"401", "402", "403", "404", "405"};
    
    //Pre-set variables for SQL connection
    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet resultSet = null;
    
    //Variables for date difference
    public long diff;
    public long DaysDiff;
    
    //Variables for timer
    long timeDifferenceInHours;
    Timer timer;
    long hour, minute, second;
    String ddSecond, ddMinute, ddHour;
    DecimalFormat dFormat = new DecimalFormat("00");
    
    //Variables for lock
    Timer lock_timer;
    int lockSecond;
    
    //Variable for random number
    public int randomNumber;
    
    //Variables for PDF
    String pdfTax;
    String pdfSubtotal;
    String pdfTotal;
    
    public Final_Frame() {
        //Auto-generated codes
        initComponents();
        
        //Background picture
        backgroundPicture();
        
        //Method for logo
        mainIcon();
        
        //Taskbar Icon
        taskbarIcon();
        
        //Random number generator
        randomNumForReferenceNumber();
        
        //Updating the rows of your table by getting the stored values in the database everytime you open this application
        updateDB();
        
        //lockCountdown
        lockTimer();
        lock_timer.start();
        
        //Table design
        mainTable.getTableHeader().setDefaultRenderer(new TableDarkHeader());
        mainTable.setDefaultRenderer(Object.class, new TableDarkCell());
    }
    private void finalFrameDispose() {
        lockJDialog dialog = new lockJDialog(this, true);
        dialog.setVisible(true);
    }
    //Lock Timer
    private void lockTimer() {
        lock_timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lockSecond++;
                try {
                    pst = connection.prepareStatement("SELECT time FROM hotel_management_system_lock ORDER BY Lock_ID DESC LIMIT 1");
                    ResultSet resultSet2 = pst.executeQuery();
                    while (resultSet2.next()) {
                        if (Integer.parseInt(resultSet2.getString("time")) >= 5) {
                            finalFrameDispose();
                            lock_timer.stop();
                            break;
                        }
                    }
                } catch (SQLException eee) {
                    JOptionPane.showMessageDialog(null, eee);
                }
                if (lockSecond == 5) {
                    try {
                        pst = connection.prepareStatement("INSERT INTO hotel_management_system_lock(time) VALUES (?)");
                        pst.setString(1, String.valueOf(lockSecond));
                        pst.execute();
                    } catch (SQLException ee) {
                        JOptionPane.showMessageDialog(null, ee);
                    }
                    
                    finalFrameDispose();
                    lock_timer.stop();
                }
            }
        });
    }
    //Timer
    private void simpleTimer(long timeDifferenceInHours) {
        //Updates the timer every one second...1 = 1 millisecond, 1000 = 1 second
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Format of second, minute, and hour
                ddSecond = dFormat.format(second);
                ddMinute = dFormat.format(minute);
                ddHour = dFormat.format(timeDifferenceInHours - 1);
                
                timerLabel.setText(ddHour + ":" + ddMinute + ":" + ddSecond);
                second--;
                if (second == -1) {
                    second = 59;
                    minute--;
                    timerLabel.setText(ddHour + ":" + ddMinute + ":" + ddSecond);
                }
                if (minute == -1) {
                    minute = 59;
                    hour--;
                    timerLabel.setText(ddHour + ":" + ddMinute + ":" + ddSecond);
                }
                if (minute == 0 && second == 0) {
                    timer.stop();
                }
            }
        });
    }
    //Background pictures
    private void backgroundPicture() {
        ImageIcon icon = new ImageIcon(getClass().getResource("Images/212849.jpg"));
        JLabel label = new JLabel();
        label.setSize(1606, 775);
        label.setIcon(icon);
        this.add(label);
    }
    //Taskbar Icon
    private void taskbarIcon() {
        ImageIcon icon2 = new ImageIcon(getClass().getResource("Images/hotel.png"));
        Image img = icon2.getImage();
        this.setIconImage(img);
    }
    //Method for random number generator for "referenceNumber"
    private void randomNumForReferenceNumber() {
        Random rand = new Random();
        
        do {
            randomNumber = rand.nextInt(700000);
        } while( randomNumber <= 100000 );
        
        referenceNumber.setText(Integer.toString(randomNumber));
        referenceNumber.setEditable(false);
    }
    //Method for database configuration
    private void updateDB() {
        int numColumn;
        
        try {
            //Connecting to the database
            connection = connector.getConnection();
            
            //SQL statement
            pst = connection.prepareCall(""
                    + "SELECT * FROM hotel_management_system_main");
            
            //gets the data produced by the query
            resultSet = pst.executeQuery();
            
            //getting the Column Count
            ResultSetMetaData rsmData = resultSet.getMetaData();
            numColumn = rsmData.getColumnCount();
            
            //Converting JTable to DefaultTableModel
            DefaultTableModel recordTable = (DefaultTableModel) mainTable.getModel();
            
            //Setting the default number of rows to zero
            recordTable.setRowCount(0);
            
            //Inserting values into the row consecutively relative to the order of the column
            while(resultSet.next()) {
                Vector columnData = new Vector();
                
                for (int i = 1; i <= numColumn; i++) {
                    columnData.add(resultSet.getString("reference_number"));
                    columnData.add(resultSet.getString("name"));
                    columnData.add(resultSet.getString("room_type"));
                    columnData.add(resultSet.getString("room_number"));
                    columnData.add(resultSet.getString("check_in_date"));
                    columnData.add(resultSet.getString("check_out_date"));
                    columnData.add(resultSet.getString("remaining_time"));
                }
                recordTable.addRow(columnData);
            }
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //Method for table design 1.0
    private class TableDarkHeader extends DefaultTableCellRenderer {
        //Designing the JTable part 1
        @Override
        public Component getTableCellRendererComponent(JTable mainTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component com = super.getTableCellRendererComponent(mainTable, value, isSelected, hasFocus, row, column); 
            com.setBackground(new Color(15, 15, 15));
            com.setForeground(new Color(218,165,32));
            com.setFont(new Font("Times New Roman", Font.PLAIN, 13));
            
            return com;
        }
    }
    //Method for table design 1.2
    private class TableDarkCell extends DefaultTableCellRenderer {
        //Designing the JTable part 2
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (mainTable.isCellSelected(row, column)) {
                com.setBackground(new Color(70, 70, 70));
            }
            else {
                if (row % 2 == 0) {
                    com.setBackground(new Color(55, 55, 55));
                }
                else {
                    com.setBackground(new Color(15, 15, 15));
                }
            }
            com.setFont(new Font("Times New Roman", Font.PLAIN, 13));
            com.setForeground(new Color(230, 230, 230));
            
            return com;
        }
    }
    
    //Method for logo
    private void mainIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("Images/ori_53987_4d16ea7d8b9a6d2b8d146316ad15f33a85d19963_luxury-hotel-logo.jpg"));
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(mainIcon.getWidth(), mainIcon.getHeight(), Image.SCALE_SMOOTH);
        mainIcon.setIcon(new ImageIcon(imgScale));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        referenceNumber = new javax.swing.JTextField();
        firstName = new javax.swing.JTextField();
        lastName = new javax.swing.JTextField();
        address = new javax.swing.JTextField();
        referenceAddress = new javax.swing.JTextField();
        contactNumber = new javax.swing.JTextField();
        emailAddress = new javax.swing.JTextField();
        nationality = new com.toedter.components.JLocaleChooser();
        birthDate = new com.toedter.calendar.JDateChooser();
        gender = new javax.swing.JComboBox<>();
        checkInDate = new com.toedter.calendar.JDateChooser();
        checkOutDate = new com.toedter.calendar.JDateChooser();
        roomType = new javax.swing.JComboBox<>();
        roomNumber = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        mainIcon = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        meals = new javax.swing.JComboBox<>();
        clear = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        specialRequest = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        tax = new javax.swing.JTextField();
        subtotal = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainTable = new javax.swing.JTable();
        viewMore = new javax.swing.JButton();
        printReceipt = new javax.swing.JButton();
        timerLabel = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(12, 12, 12));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(218, 165, 32));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Reference number:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(218, 165, 32));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("First name:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(218, 165, 32));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Last name:");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(218, 165, 32));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Address:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(218, 165, 32));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Reference address:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(218, 165, 32));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Contact number:");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(218, 165, 32));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Email address:");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(218, 165, 32));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Nationality:");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(218, 165, 32));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Birth date:");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(218, 165, 32));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Gender:");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(218, 165, 32));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Check in date:");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(218, 165, 32));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Check out date:");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(218, 165, 32));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Room type:");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(218, 165, 32));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Room number:");

        referenceNumber.setBackground(new java.awt.Color(110, 115, 119));
        referenceNumber.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        referenceNumber.setForeground(new java.awt.Color(0, 0, 0));
        referenceNumber.setToolTipText("Enter Value");
        referenceNumber.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        referenceNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referenceNumberActionPerformed(evt);
            }
        });

        firstName.setBackground(new java.awt.Color(110, 115, 119));
        firstName.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        firstName.setForeground(new java.awt.Color(0, 0, 0));
        firstName.setToolTipText("Enter Value");

        lastName.setBackground(new java.awt.Color(110, 115, 119));
        lastName.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lastName.setForeground(new java.awt.Color(0, 0, 0));
        lastName.setToolTipText("Enter Value");

        address.setBackground(new java.awt.Color(110, 115, 119));
        address.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        address.setForeground(new java.awt.Color(0, 0, 0));
        address.setToolTipText("Enter Value");

        referenceAddress.setBackground(new java.awt.Color(110, 115, 119));
        referenceAddress.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        referenceAddress.setForeground(new java.awt.Color(0, 0, 0));
        referenceAddress.setToolTipText("Enter Value");

        contactNumber.setBackground(new java.awt.Color(110, 115, 119));
        contactNumber.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        contactNumber.setForeground(new java.awt.Color(0, 0, 0));
        contactNumber.setToolTipText("Enter Value");

        emailAddress.setBackground(new java.awt.Color(110, 115, 119));
        emailAddress.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        emailAddress.setForeground(new java.awt.Color(0, 0, 0));
        emailAddress.setToolTipText("Enter Value");

        nationality.setBackground(new java.awt.Color(110, 115, 119));
        nationality.setForeground(new java.awt.Color(0, 0, 0));
        nationality.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Filipino (Philippines)", "Thai (Thai, Thailand)", "Turkmen (Latin, Turkmenistan)", "Tigrinya (Ethiopia)", "Tamil (Singapore)", "English (Niue)", "Chinese (Simplified, Singapore)", "Fulah (Adlam, Liberia)", "English (Jamaica)", "Northern Sami (Latin, Norway)", "Spanish (Bolivia)", "Dzongkha (Bhutan)", "Bosnian (Bosnia & Herzegovina)", "Lower Sorbian (Germany)", "Luxembourgish (Latin, Luxembourg)", "English (Liberia)", "Arabic (Chad)", "Nuer (Latin, South Sudan)", "Fulah (Latin, Mauritania)", "Swahili (Uganda)", "Turkmen (Turkmenistan)", "Serbian (Cyrillic, Montenegro)", "Arabic (Egypt)", "Ossetic (Cyrillic, Georgia)", "Yoruba (Nigeria)", "Northern Sami (Norway)", "English (Palau)", "Polish (Poland)", "Serbian (Serbia And Montenegro)", "Nepali (India)", "Aghem (Latin, Cameroon)", "Arabic (Western Sahara)", "Bosnian (Latin, Bosnia & Herzegovina)", "Low German (Germany)", "Norwegian Bokmål (Svalbard & Jan Mayen)", "Spanish (United States)", "English (United States, Computer)", "Portuguese (Macao SAR China)", "Luxembourgish (Luxembourg)", "Lower Sorbian (Latin, Germany)", "Icelandic (Latin, Iceland)", "Somali (Kenya)", "Zarma (Niger)", "Armenian (Armenian, Armenia)", "Fulah (Latin, Ghana)", "French (St. Pierre & Miquelon)", "Arabic (Comoros)", "Norwegian (Norway, Nynorsk)", "Polish (Latin, Poland)", "Aghem (Cameroon)", "Gujarati (Gujarati, India)", "Turkish (Turkey)", "Kalaallisut (Latin, Greenland)", "Arabic (Mauritania)", "Spanish (Dominican Republic)", "Kalaallisut (Greenland)", "English (Nauru)", "Low German (Latin, Germany)", "Kinyarwanda (Latin, Rwanda)", "English (Australia)", "English (Cyprus)", "Turkish (Latin, Turkey)", "Tigrinya (Eritrea)", "Nuer (South Sudan)", "English (Rwanda)", "Croatian (Latin, Croatia)", "Lingala (Congo - Kinshasa)", "Ngiemboon (Cameroon)", "Zarma (Latin, Niger)", "Arabic (Israel)", "Odia (India)", "Pashto (Arabic, Afghanistan)", "Arabic (Syria)", "Luo (Latin, Kenya)", "Bafia (Cameroon)", "Tatar (Russia)", "Oromo (Kenya)", "English (American Samoa)", "French (Vanuatu)", "Chinese (Taiwan)", "Sindhi (India)", "Ngiemboon (Latin, Cameroon)", "Portuguese (Mozambique)", "French (Niger)", "Friulian (Latin, Italy)", "Asturian (Latin, Spain)", "Bena (Latin, Tanzania)", "Shambala (Tanzania)", "Burmese (Myanmar, Myanmar (Burma))", "English (Jersey)", "Lingala (Central African Republic)", "Uzbek (Afghanistan)", "Danish (Latin, Denmark)", "English (Christmas Island)", "Akan (Latin, Ghana)", "English (Austria)", "Cantonese (Simplified, China)", "Kannada (India)", "English (Tanzania)", "English (Puerto Rico)", "French (New Caledonia)", "Scottish Gaelic (United Kingdom)", "French (Cameroon)", "Walser (Latin, Switzerland)", "Punjabi (Gurmukhi, India)", "Urdu (Arabic, Pakistan)", "Nigerian Pidgin (Latin, Nigeria)", "Teso (Uganda)", "Norwegian (Latin, Norway)", "Bangla (Bangla, Bangladesh)", "Inari Sami (Latin, Finland)", "English (Netherlands)", "Lingala (Congo - Brazzaville)", "Nepali (Devanagari, Nepal)", "Azerbaijani (Azerbaijan)", "Marathi (India)", "Greek (Cyprus)", "Kurdish (Turkey)", "Gusii (Latin, Kenya)", "Northern Luri (Iran)", "Swiss German (France)", "Spanish (Honduras)", "Hungarian (Hungary)", "Fulah (Senegal)", "Albanian (North Macedonia)", "Serbian (Cyrillic, Bosnia & Herzegovina)", "Bulgarian (Cyrillic, Bulgaria)", "Somali (Latin, Somalia)", "Estonian (Estonia)", "Arabic (Oman)", "Western Frisian (Netherlands)", "Turkish (Cyprus)", "Latvian (Latin, Latvia)", "Uzbek (Latin, Uzbekistan)", "Northern Luri (Iraq)", "Duala (Cameroon)", "German (Italy)", "Portuguese (Latin, Brazil)", "French (Tunisia)", "Serbian (Serbia)", "German (Switzerland)", "Swahili (Latin, Tanzania)", "French (French Polynesia)", "Portuguese (Equatorial Guinea)", "Vunjo (Tanzania)", "Machame (Tanzania)", "Malagasy (Latin, Madagascar)", "English (Tuvalu)", "English (Pitcairn Islands)", "Luba-Katanga (Latin, Congo - Kinshasa)", "Chinese (Simplified, Hong Kong SAR China)", "Dutch (Netherlands)", "English (Guyana)", "Jola-Fonyi (Senegal)", "Dutch (Curaçao)", "Arabic (Algeria)", "Portuguese (Switzerland)", "French (Equatorial Guinea)", "English (Nigeria)", "French (Côte d’Ivoire)", "Kikuyu (Kenya)", "Interlingua (world)", "English (Pakistan)", "Chinese (China)", "English (St. Lucia)", "Bodo (India)", "Fulah (Adlam, Burkina Faso)", "Manx (Isle of Man)", "Cornish (Latin, United Kingdom)", "Bambara (Latin, Mali)", "Yangben (Latin, Cameroon)", "Macedonian (North Macedonia)", "English (Trinidad & Tobago)", "Slovenian (Slovenia)", "Sango (Latin, Central African Republic)", "Finnish (Latin, Finland)", "Xhosa (South Africa)", "French (Belgium)", "Ngomba (Cameroon)", "Fulah (Adlam, Niger)", "Spanish (Venezuela)", "Meru (Kenya)", "English (Bermuda)", "Norwegian Bokmål (Norway)", "Kabuverdianu (Cape Verde)", "Vietnamese (Vietnam)", "English (United States)", "Morisyen (Mauritius)", "French (Burkina Faso)", "Italian (San Marino)", "Persian (Arabic, Iran)", "Sanskrit (Devanagari, India)", "French (Mayotte)", "Gujarati (India)", "Fulah (Latin, Cameroon)", "Maltese (Latin, Malta)", "Finnish (Finland)", "Catalan (France)", "Serbian (Latin, Bosnia & Herzegovina)", "Chakma (Chakma, Bangladesh)", "Ukrainian (Cyrillic, Ukraine)", "French (Djibouti)", "Fulah (Adlam, Guinea-Bissau)", "Hausa (Ghana)", "Yiddish (Hebrew, world)", "Afrikaans (Latin, South Africa)", "Tigrinya (Ethiopic, Ethiopia)", "Tongan (Latin, Tonga)", "German (Belgium)", "Amharic (Ethiopic, Ethiopia)", "Fulah (Latin, Guinea-Bissau)", "Chinese (Macao SAR China)", "Mundang (Latin, Cameroon)", "Nyankole (Uganda)", "Rundi (Latin, Burundi)", "Malay (Malaysia)", "Tamil (Sri Lanka)", "German (Latin, Germany)", "Cebuano (Philippines)", "Tajik (Tajikistan)", "Vunjo (Latin, Tanzania)", "Quechua (Peru)", "Spanish (Ecuador)", "Macedonian (Cyrillic, North Macedonia)", "Fulah (Adlam, Cameroon)", "Ganda (Uganda)", "Fulah (Latin, Niger)", "Zulu (South Africa)", "French (Latin, France)", "Sundanese (Latin, Indonesia)", "Chiga (Uganda)", "Colognian (Germany)", "Russian (Russia)", "Nigerian Pidgin (Nigeria)", "English (Dominica)", "Hebrew (Hebrew, Israel)", "English (Burundi)", "Irish (Ireland)", "Cantonese (China)", "Romansh (Latin, Switzerland)", "English (Samoa)", "French (Luxembourg)", "Maori (New Zealand)", "Arabic (Eritrea)", "Shona (Zimbabwe)", "Spanish (Ceuta & Melilla)", "Irish (United Kingdom)", "Thai (Thailand, TH, Thai Digits)", "French (Seychelles)", "English (Sierra Leone)", "Fulah (Latin, Nigeria)", "Norwegian (Norway)", "Fulah (Adlam, Nigeria)", "Chinese (Traditional, Macao SAR China)", "English (St. Helena)", "Sindhi (Arabic, Pakistan)", "German (Luxembourg)", "German (Germany)", "Norwegian Nynorsk (Latin, Norway)", "English (Denmark)", "Somali (Djibouti)", "Lithuanian (Lithuania)", "Esperanto (world)", "Pashto (Pakistan)", "English (US Outlying Islands)", "English (Slovenia)", "Vai (Vai, Liberia)", "Lao (Laos)", "Rombo (Latin, Tanzania)", "Arabic (Lebanon)", "Northern Luri (Arabic, Iran)", "Afrikaans (South Africa)", "Fulah (Guinea)", "Colognian (Latin, Germany)", "Spanish (Belize)", "Arabic (United Arab Emirates)", "Esperanto (Latin, world)", "Croatian (Croatia)", "Rombo (Tanzania)", "Kashmiri (India)", "Burmese (Myanmar (Burma))", "Mongolian (Mongolia)", "Urdu (Pakistan)", "Danish (Denmark)", "English (Micronesia)", "English (Belgium)", "French (Wallis & Futuna)", "Mazanderani (Iran)", "English (Singapore)", "Kalenjin (Kenya)", "Fulah (Latin, Gambia)", "Tatar (Cyrillic, Russia)", "Teso (Latin, Uganda)", "Asu (Latin, Tanzania)", "Bena (Tanzania)", "Fulah (Latin, Guinea)", "Slovenian (Latin, Slovenia)", "English (Sudan)", "Maithili (Devanagari, India)", "Japanese (Japan, JP, Japanese Calendar)", "Spanish (El Salvador)", "Portuguese (Brazil)", "Manipuri (Bangla, India)", "Malayalam (India)", "Irish (Latin, Ireland)", "English (Falkland Islands)", "Icelandic (Iceland)", "Basaa (Latin, Cameroon)", "English (Diego Garcia)", "Portuguese (São Tomé & Príncipe)", "Akan (Ghana)", "Uzbek (Arabic, Afghanistan)", "English (Sweden)", "Chinese (Simplified, China)", "Spanish (Latin America)", "Lingala (Latin, Congo - Kinshasa)", "Kako (Latin, Cameroon)", "Gusii (Kenya)", "Swiss German (Liechtenstein)", "Chakma (Bangladesh)", "Spanish (Canary Islands)", "Arabic (Tunisia)", "Bambara (Mali)", "Cornish (United Kingdom)", "Punjabi (Pakistan)", "Uyghur (China)", "Machame (Latin, Tanzania)", "Kikuyu (Latin, Kenya)", "Spanish (Brazil)", "Koyra Chiini (Mali)", "English (Solomon Islands)", "Tibetan (Tibetan, China)", "Cherokee (United States)", "Kinyarwanda (Rwanda)", "Tachelhit (Tifinagh, Morocco)", "Arabic (Iraq)", "English (Germany)", "Romanian (Moldova)", "English (Finland)", "Uzbek (Uzbekistan)", "Taita (Kenya)", "Interlingua (Latin, world)", "Yakut (Cyrillic, Russia)", "English (Seychelles)", "English (Uganda)", "English (New Zealand)", "Spanish (Uruguay)", "Masai (Kenya)", "Manipuri (India)", "Russian (Ukraine)", "Sango (Central African Republic)", "English (Fiji)", "German (Liechtenstein)", "English (Barbados)", "Inari Sami (Finland)", "Croatian (Bosnia & Herzegovina)", "Yoruba (Latin, Nigeria)", "German (Austria)", "Luba-Katanga (Congo - Kinshasa)", "Arabic (world)", "Somali (Somalia)", "Latvian (Latvia)", "Arabic (Kuwait)", "Serbian (Cyrillic, Serbia)", "English (Lesotho)", "English (Hong Kong SAR China)", "Bodo (Devanagari, India)", "Meru (Latin, Kenya)", "Chechen (Russia)", "Georgian (Georgia)", "Swahili (Tanzania)", "French (Rwanda)", "Malagasy (Madagascar)", "English (Latin, United States)", "Ossetic (Russia)", "Serbian (Latin, Serbia)", "Kyrgyz (Kyrgyzstan)", "Central Atlas Tamazight (Latin, Morocco)", "Dutch (Latin, Netherlands)", "Kurdish (Latin, Turkey)", "Morisyen (Latin, Mauritius)", "Arabic (Jordan)", "Galician (Latin, Spain)", "Kyrgyz (Cyrillic, Kyrgyzstan)", "Amharic (Ethiopia)", "French (Algeria)", "Tibetan (China)", "Quechua (Ecuador)", "Georgian (Georgian, Georgia)", "English (Montserrat)", "Konkani (Devanagari, India)", "English (Guernsey)", "Makonde (Latin, Tanzania)", "Santali (India)", "Swedish (Sweden)", "Serbian (Montenegro)", "English (Zambia)", "French (Mali)", "Hausa (Nigeria)", "Arabic (Saudi Arabia)", "Persian (Afghanistan)", "Filipino (Latin, Philippines)", "Oromo (Latin, Ethiopia)", "Ossetic (Georgia)", "Central Kurdish (Arabic, Iraq)", "Yiddish (world)", "English (Malta)", "English (Ghana)", "English (Israel)", "Kannada (Kannada, India)", "Central Atlas Tamazight (Morocco)", "Greek (Greek, Greece)", "Koyraboro Senni (Mali)", "Rwa (Latin, Tanzania)", "Lakota (United States)", "Vai (Latin, Liberia)", "Swahili (Congo - Kinshasa)", "Fulah (Adlam, Mauritania)", "Italian (Vatican City)", "Spanish (Philippines)", "Spanish (Spain)", "Spanish (Colombia)", "Bulgarian (Bulgaria)", "English (St. Vincent & Grenadines)", "Koyraboro Senni (Latin, Mali)", "Upper Sorbian (Germany)", "Basque (Latin, Spain)", "English (Europe)", "Swedish (Latin, Sweden)", "Arabic (Sudan)", "Hausa (Niger)", "English (St. Kitts & Nevis)", "Romanian (Romania)", "Serbian (Latin, Montenegro)", "Spanish (Guatemala)", "Fulah (Latin, Liberia)", "Basaa (Cameroon)", "North Ndebele (Latin, Zimbabwe)", "French (Madagascar)", "Spanish (Chile)", "Kamba (Kenya)", "Persian (Iran)", "English (Macao SAR China)", "Japanese (Japanese, Japan)", "English (Belize)", "Lakota (Latin, United States)", "Albanian (Albania)", "Romanian (Latin, Romania)", "Tasawaq (Niger)", "Kwasio (Cameroon)", "Tamil (Tamil, India)", "Kabyle (Latin, Algeria)", "English (Northern Mariana Islands)", "English (Grenada)", "Sangu (Latin, Tanzania)", "English (Botswana)", "Kabyle (Algeria)", "Makonde (Tanzania)", "Hebrew (Israel)", "Tamil (Malaysia)", "Swedish (Finland)", "North Ndebele (Zimbabwe)", "Luyia (Latin, Kenya)", "Sinhala (Sinhala, Sri Lanka)", "English (Ireland)", "Chinese (Singapore)", "English (Kiribati)", "Tasawaq (Latin, Niger)", "Oromo (Ethiopia)", "Lao (Lao, Laos)", "Albanian (Latin, Albania)", "Japanese (Japan)", "Kamba (Latin, Kenya)", "Fulah (Adlam, Ghana)", "Korean (Korean, South Korea)", "French (St. Martin)", "Malay (Indonesia)", "Sanskrit (India)", "Cantonese (Hong Kong SAR China)", "English (Eswatini)", "Faroese (Latin, Faroe Islands)", "Rwa (Tanzania)", "Spanish (Peru)", "Makhuwa-Meetto (Mozambique)", "English (United Kingdom)", "Chinese (Traditional, Hong Kong SAR China)", "Santali (Ol Chiki, India)", "Kazakh (Kazakhstan)", "Spanish (Panama)", "Arabic (Palestinian Territories)", "French (Monaco)", "Urdu (India)", "Chechen (Cyrillic, Russia)", "Swiss German (Switzerland)", "Luyia (Kenya)", "Kako (Cameroon)", "Spanish (Equatorial Guinea)", "Makhuwa-Meetto (Latin, Mozambique)", "Arabic (Yemen)", "English (Sint Maarten)", "Russian (Kazakhstan)", "Korean (North Korea)", "Dutch (Suriname)", "English (Bahamas)", "Bemba (Latin, Zambia)", "Kalenjin (Latin, Kenya)", "Chiga (Latin, Uganda)", "Dutch (Caribbean Netherlands)", "Ewe (Latin, Ghana)", "Fulah (Adlam, Guinea)", "Uzbek (Cyrillic, Uzbekistan)", "Hindi (Devanagari, India)", "English (Kenya)", "Asu (Tanzania)", "French (Senegal)", "French (Morocco)", "Portuguese (Luxembourg)", "Fulah (Adlam, Gambia)", "French (St. Barthélemy)", "Metaʼ (Cameroon)", "Kwasio (Latin, Cameroon)", "Maithili (India)", "Korean (South Korea)", "Tajik (Cyrillic, Tajikistan)", "Spanish (Mexico)", "Zulu (Latin, South Africa)", "Dogri (India)", "English (South Sudan)", "Tibetan (India)", "Tachelhit (Morocco)", "English (Madagascar)", "French (Burundi)", "Upper Sorbian (Latin, Germany)", "Nama (Latin, Namibia)", "Breton (Latin, France)", "Bangla (Bangladesh)", "Spanish (Latin, Spain)", "English (South Africa)", "Welsh (Latin, United Kingdom)", "French (France)", "Punjabi (India)", "English (Marshall Islands)", "French (Benin)", "Vai (Liberia)", "Javanese (Latin, Indonesia)", "Yakut (Russia)", "Pashto (Afghanistan)", "Bosnian (Cyrillic, Bosnia & Herzegovina)", "Xhosa (Latin, South Africa)", "Taita (Latin, Kenya)", "Slovak (Slovakia)", "Koyra Chiini (Latin, Mali)", "Mazanderani (Arabic, Iran)", "Mongolian (Cyrillic, Mongolia)", "Wolof (Senegal)", "Hausa (Latin, Nigeria)", "French (Haiti)", "Russian (Cyrillic, Russia)", "Telugu (India)", "Masai (Latin, Kenya)", "Dutch (Sint Maarten)", "French (Congo - Brazzaville)", "Manx (Latin, Isle of Man)", "Maltese (Malta)", "Malay (Latin, Malaysia)", "Malayalam (Malayalam, India)", "Low German (Netherlands)", "Standard Moroccan Tamazight (Tifinagh, Morocco)", "Nyankole (Latin, Uganda)", "English (Vanuatu)", "Tongan (Tonga)", "Fulah (Latin, Sierra Leone)", "Sena (Mozambique)", "Soga (Latin, Uganda)", "Fulah (Adlam, Senegal)", "Vietnamese (Latin, Vietnam)", "Sundanese (Indonesia)", "Ngomba (Latin, Cameroon)", "Sichuan Yi (China)", "Punjabi (Arabic, Pakistan)", "French (Réunion)", "Bangla (India)", "French (Guadeloupe)", "Standard Moroccan Tamazight (Morocco)", "Ukrainian (Ukraine)", "Ganda (Latin, Uganda)", "Cebuano (Latin, Philippines)", "English (Norfolk Island)", "French (Switzerland)", "Serbian (Cyrillic, Kosovo)", "Norwegian Bokmål (Latin, Norway)", "Arabic (South Sudan)", "English (Guam)", "Dutch (Aruba)", "English (Anguilla)", "Western Frisian (Latin, Netherlands)", "Soga (Uganda)", "English (Cameroon)", "Czech (Czechia)", "Catalan (Spain)", "Hungarian (Latin, Hungary)", "Romansh (Switzerland)", "Russian (Moldova)", "Fulah (Latin, Senegal)", "English (Tonga)", "Fulah (Adlam, Sierra Leone)", "English (Papua New Guinea)", "Estonian (Latin, Estonia)", "French (Central African Republic)", "Portuguese (Timor-Leste)", "English (Eritrea)", "Serbian (Bosnia & Herzegovina)", "Spanish (Paraguay)", "Konkani (India)", "Belarusian (Cyrillic, Belarus)", "French (Togo)", "Dzongkha (Tibetan, Bhutan)", "Serbian (Latin, Kosovo)", "English (Philippines)", "Igbo (Nigeria)", "French (Guinea)", "Scottish Gaelic (Latin, United Kingdom)", "Chinese (Simplified, Macao SAR China)", "Northern Sami (Finland)", "English (Cook Islands)", "Arabic (Morocco)", "Hawaiian (Latin, United States)", "English (Antigua & Barbuda)", "French (Chad)", "Embu (Kenya)", "Lithuanian (Latin, Lithuania)", "Bemba (Zambia)", "Ewondo (Latin, Cameroon)", "Ewondo (Cameroon)", "French (Congo - Kinshasa)", "Rundi (Burundi)", "English (Namibia)", "Metaʼ (Latin, Cameroon)", "Catalan (Italy)", "Quechua (Latin, Peru)", "Langi (Tanzania)", "Indonesian (Indonesia)", "Teso (Kenya)", "Catalan (Andorra)", "Quechua (Bolivia)", "Hawaiian (United States)", "French (Canada)", "Kabuverdianu (Latin, Cape Verde)", "Albanian (Kosovo)", "Maori (Latin, New Zealand)", "English (Cayman Islands)", "Italian (Switzerland)", "Sinhala (Sri Lanka)", "Luo (Kenya)", "English (United Arab Emirates)", "Italian (Italy)", "Arabic (Somalia)", "English (Zimbabwe)", "Norwegian Nynorsk (Norway)", "French (Mauritius)", "Northern Sami (Sweden)", "English (Tokelau)", "Mundang (Cameroon)", "Samburu (Kenya)", "Portuguese (Guinea-Bissau)", "Shona (Latin, Zimbabwe)", "Malay (Singapore)", "Ewe (Togo)", "Lingala (Angola)", "Belarusian (Belarus)", "Khmer (Khmer, Cambodia)", "Italian (Latin, Italy)", "Portuguese (Cape Verde)", "Spanish (Puerto Rico)", "Walser (Switzerland)", "Russian (Belarus)", "Faroese (Denmark)", "Ewe (Ghana)", "Arabic (Bahrain)", "Kazakh (Cyrillic, Kazakhstan)", "Uyghur (Arabic, China)", "Hindi (India)", "English (Switzerland)", "Samburu (Latin, Kenya)", "Faroese (Faroe Islands)", "Yoruba (Benin)", "Asturian (Spain)", "French (Comoros)", "French (Martinique)", "Spanish (Argentina)", "Dogri (Devanagari, India)", "English (Malaysia)", "Sangu (Tanzania)", "Embu (Latin, Kenya)", "Cantonese (Traditional, Hong Kong SAR China)", "Armenian (Armenia)", "English (Gambia)", "Shambala (Latin, Tanzania)", "Nepali (Nepal)", "Friulian (Italy)", "Tamil (India)", "French (French Guiana)", "Indonesian (Latin, Indonesia)", "Portuguese (Angola)", "Telugu (Telugu, India)", "English (world)", "Duala (Latin, Cameroon)", "Javanese (Indonesia)", "Langi (Latin, Tanzania)", "Russian (Kyrgyzstan)", "French (Mauritania)", "Bafia (Latin, Cameroon)", "Fulah (Latin, Burkina Faso)", "Chinese (Traditional, Taiwan)", "Assamese (India)", "Chinese (Hong Kong SAR China)", "Swahili (Kenya)", "Assamese (Bangla, India)", "Thai (Thailand)", "English (Malawi)", "Nama (Namibia)", "English (British Indian Ocean Territory)", "Arabic (Qatar)", "Arabic (Arabic, Egypt)", "English (Cocos (Keeling) Islands)", "Portuguese (Portugal)", "Slovak (Latin, Slovakia)", "Azerbaijani (Cyrillic, Azerbaijan)", "Central Kurdish (Iraq)", "Tachelhit (Latin, Morocco)", "Spanish (Cuba)", "English (US Virgin Islands)", "Basque (Spain)", "Igbo (Latin, Nigeria)", "Greek (Greece)", "Yangben (Cameroon)", "Danish (Greenland)", "Khmer (Cambodia)", "Central Kurdish (Iran)", "Kashmiri (Arabic, India)", "Catalan (Spain, Valencian)", "Wolof (Latin, Senegal)", "Sindhi (Devanagari, India)", "Marathi (Devanagari, India)", "Spanish (Costa Rica)", "French (Gabon)", "Arabic (Libya)", "English (Mauritius)", "Galician (Spain)", "Azerbaijani (Latin, Azerbaijan)", "English (Isle of Man)", "Swiss German (Latin, Switzerland)", "Sena (Latin, Mozambique)", "English (Gibraltar)", "English (Canada)", "Cherokee (Cherokee, United States)", "French (Syria)", "Odia (Odia, India)", "Somali (Ethiopia)", "Catalan (Latin, Spain)", "Dutch (Belgium)", "Arabic (Djibouti)", "Jola-Fonyi (Latin, Senegal)", "Welsh (United Kingdom)", "English (British Virgin Islands)", "English (Turks & Caicos Islands)", "Swedish (Åland Islands)", "Czech (Latin, Czechia)", "Afrikaans (Namibia)", "English (India)", "Spanish (Nicaragua)", "Sichuan Yi (Yi, China)", "Sindhi (Pakistan)", "Masai (Tanzania)", "Malay (Brunei)", "Chakma (India)", "Breton (France)" }));
        nationality.setToolTipText("Choose Value");

        birthDate.setBackground(new java.awt.Color(110, 115, 119));
        birthDate.setForeground(new java.awt.Color(0, 0, 0));
        birthDate.setToolTipText("Choose Value");

        gender.setBackground(new java.awt.Color(110, 115, 119));
        gender.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        gender.setForeground(new java.awt.Color(0, 0, 0));
        gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        gender.setToolTipText("Choose Value");

        checkInDate.setBackground(new java.awt.Color(110, 115, 119));
        checkInDate.setForeground(new java.awt.Color(0, 0, 0));
        checkInDate.setToolTipText("Choose Value");

        checkOutDate.setBackground(new java.awt.Color(110, 115, 119));
        checkOutDate.setForeground(new java.awt.Color(0, 0, 0));
        checkOutDate.setToolTipText("Choose Value");

        roomType.setBackground(new java.awt.Color(110, 115, 119));
        roomType.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        roomType.setForeground(new java.awt.Color(0, 0, 0));
        roomType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Single", "Double", "Guest", "VIP" }));
        roomType.setToolTipText("Choose Value");
        roomType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomTypeActionPerformed(evt);
            }
        });

        roomNumber.setBackground(new java.awt.Color(110, 115, 119));
        roomNumber.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        roomNumber.setForeground(new java.awt.Color(0, 0, 0));
        roomNumber.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "101", "102", "103", "104", "105" }));
        roomNumber.setToolTipText("Choose Value");
        roomNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomNumberActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(218, 165, 32));
        jSeparator1.setForeground(new java.awt.Color(218, 165, 32));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(218, 165, 32));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Meals per day:");

        meals.setBackground(new java.awt.Color(110, 115, 119));
        meals.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        meals.setForeground(new java.awt.Color(0, 0, 0));
        meals.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "One meal", "Two meals", "Three meals" }));
        meals.setToolTipText("Choose Value");

        clear.setBackground(new java.awt.Color(34, 35, 37));
        clear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        clear.setForeground(new java.awt.Color(218, 165, 32));
        clear.setText("CLEAR");
        clear.setToolTipText("Clear all values");
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(218, 165, 32));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Special request:");

        specialRequest.setColumns(20);
        specialRequest.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        specialRequest.setRows(5);
        jScrollPane2.setViewportView(specialRequest);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(referenceNumber)
                            .addComponent(firstName)
                            .addComponent(lastName)
                            .addComponent(address)
                            .addComponent(referenceAddress)
                            .addComponent(contactNumber)
                            .addComponent(emailAddress)
                            .addComponent(nationality, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(birthDate, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                            .addComponent(gender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(checkInDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(checkOutDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roomType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roomNumber, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(meals, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(mainIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(referenceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(referenceAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(contactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(emailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(nationality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(birthDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(checkInDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(checkOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(roomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(roomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(meals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(clear)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(45, 45, 45));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(218, 165, 32));
        jLabel1.setText("X");
        jLabel1.setToolTipText("Quick exit");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(218, 165, 32));
        jLabel2.setText("-");
        jLabel2.setToolTipText("Minimize");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(1530, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(69, 1, 0));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(218, 165, 32));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Tax:");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(218, 165, 32));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Subtotal:");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(218, 165, 32));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Total:");

        tax.setEditable(false);
        tax.setBackground(new java.awt.Color(110, 115, 119));
        tax.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tax.setForeground(new java.awt.Color(0, 0, 0));
        tax.setToolTipText("Net tax");
        tax.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taxActionPerformed(evt);
            }
        });

        subtotal.setEditable(false);
        subtotal.setBackground(new java.awt.Color(110, 115, 119));
        subtotal.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        subtotal.setForeground(new java.awt.Color(0, 0, 0));
        subtotal.setToolTipText("Net subtotal");
        subtotal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        total.setEditable(false);
        total.setBackground(new java.awt.Color(110, 115, 119));
        total.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        total.setForeground(new java.awt.Color(0, 0, 0));
        total.setToolTipText("Net total");
        total.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jButton3.setBackground(new java.awt.Color(34, 35, 37));
        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(218, 165, 32));
        jButton3.setText("DELETE");
        jButton3.setToolTipText("Delete selected row");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(34, 35, 37));
        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(218, 165, 32));
        jButton5.setText("PRINT");
        jButton5.setToolTipText("Get total cost");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(34, 35, 37));
        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(218, 165, 32));
        jButton4.setText("RESET");
        jButton4.setToolTipText("Reset all inputted value");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(34, 35, 37));
        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(218, 165, 32));
        jButton6.setText("EXIT");
        jButton6.setToolTipText("Exit program");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(34, 35, 37));
        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(218, 165, 32));
        jButton7.setText("UPDATE");
        jButton7.setToolTipText("Reset value");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tax)
                    .addComponent(subtotal)
                    .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                .addGap(72, 72, 72)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(tax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton5)
                    .addComponent(jButton4)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        mainTable.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        mainTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Reference number", "Name", "Room type", "Room number", "Check in date", "Check out date", "Remaining time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mainTable.setFocusable(false);
        mainTable.setRowHeight(30);
        mainTable.setShowGrid(false);
        mainTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(mainTable);

        viewMore.setBackground(new java.awt.Color(34, 35, 37));
        viewMore.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        viewMore.setForeground(new java.awt.Color(218, 165, 32));
        viewMore.setText("VIEW MORE");
        viewMore.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewMore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewMoreActionPerformed(evt);
            }
        });

        printReceipt.setBackground(new java.awt.Color(34, 35, 37));
        printReceipt.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        printReceipt.setForeground(new java.awt.Color(218, 165, 32));
        printReceipt.setText("PRINT RECEIPT");
        printReceipt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        printReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReceiptActionPerformed(evt);
            }
        });

        timerLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timerLabelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(353, 353, 353)
                                .addComponent(viewMore, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(163, 163, 163)
                                .addComponent(printReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1203, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(132, 132, 132)
                                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewMore, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    //Method for exit
    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel1MouseClicked
    //Method for minimize
    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel2MouseClicked
    //Method for the "print" feature
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        MessageFormat header = new MessageFormat("Hotel Management System");
        MessageFormat footer = new MessageFormat("Page {0, number, integer}");
        
        try {
            mainTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch(PrinterException e) {
            System.err.format("No printer found", e.getMessage());
        }
    }//GEN-LAST:event_jButton5ActionPerformed
    //Method for exit button
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
      
        if (JOptionPane.showConfirmDialog(null, "Confirm exit?", "Hotel Management System", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jButton6ActionPerformed
    //Method for reset button
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to reset all of the inputted data?", "Hotel Management System", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
            model.setRowCount(0);

            referenceNumber.setText(null);
            firstName.setText(null);
            lastName.setText(null);
            address.setText(null);
            contactNumber.setText(null);
            emailAddress.setText(null);
            referenceAddress.setText(null);
            nationality.setSelectedIndex(0);
            gender.setSelectedIndex(0);
            roomType.setSelectedIndex(0);
            roomNumber.setSelectedIndex(0);
            birthDate.setDate(null);
            checkInDate.setDate(null);
            checkOutDate.setDate(null);
            meals.setSelectedIndex(0);
            specialRequest.setText(null);
            
            total.setText(null);
            tax.setText(null);
            subtotal.setText(null);
            
            DefaultTableModel tableModel = (DefaultTableModel) mainTable.getModel();
            tableModel.setRowCount(0);
            randomNumForReferenceNumber();

            JOptionPane.showMessageDialog(null, "All of the data have been reset successfully. Unless the Delete Function is used, all of the data will continue to exist in the database.", "Hotel Management System", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    //Method for delete button
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
           
        DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
        if (mainTable.getSelectedRow() != -1) {
            if (mainTable.getRowCount() != 0) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this row?", "Hotel Management System", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    
                    //Deleting in the database
                    try {
                        //connecting to the database
                        connection = connector.getConnection();
                        pst = connection.prepareStatement("DELETE FROM hotel_management_system_main WHERE reference_number = ?");
                        
                        //identifying the selected row
                        int selectedRows = mainTable.getSelectedRow();
                        
                        //getting the reference number of the selected row then using it to delete the row it belongs to in the database
                        int id1 = Integer.parseInt(model.getValueAt(selectedRows, 0).toString());
                        pst.setInt(1, id1);
                        pst.executeUpdate();
                        
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                    model.removeRow(mainTable.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Hotel Booking Update Confirmed", "Hotel Mangement System", JOptionPane.OK_OPTION);
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Please select a row to delete", "Hotel Management System", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    //Method for roomType JComboBox
    private void roomTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomTypeActionPerformed

        if (roomType.getSelectedItem().equals("Single")) {
            roomNumber.setModel(new DefaultComboBoxModel(singleRoom));
        }
        if (roomType.getSelectedItem().equals("Double")) {
            roomNumber.setModel(new DefaultComboBoxModel(doubleRoom));
        }
        if (roomType.getSelectedItem().equals("Guest")) {
            roomNumber.setModel(new DefaultComboBoxModel(guestRoom));
        }
        if (roomType.getSelectedItem().equals("VIP")) {
            roomNumber.setModel(new DefaultComboBoxModel(vipRoom));
        }
    }//GEN-LAST:event_roomTypeActionPerformed

    //Method for update button
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        if (
            referenceNumber.getText().equals("") ||
            firstName.getText().equals("") ||
            lastName.getText().equals("") ||
            address.getText().equals("") ||
            referenceAddress.getText().equals("") ||
            contactNumber.getText().equals("") ||
            emailAddress.getText().equals("") ||
            birthDate.getDate() == null ||
            checkInDate.getDate() == null ||
            checkOutDate.getDate() == null
                ) {
            JOptionPane.showMessageDialog(null, "Please input all the required details", "Hotel Management System", JOptionPane.WARNING_MESSAGE);
        }
        else {
            //Getting the date difference
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date d1 = null;
            Date d2 = null;

            try {
                    d1 = format.parse(format.format(checkInDate.getDate()));
                    d2 = format.parse(format.format(checkOutDate.getDate()));
            }
            catch (ParseException e) {
                JOptionPane.showMessageDialog(null, e);
            }

            diff = d2.getTime() - d1.getTime();
            DaysDiff = diff / 1000 / 60 / 60 / 24;
            
            //Setting the timer
            timeDifferenceInHours = TimeUnit.MILLISECONDS.toHours(diff);
            JOptionPane.showMessageDialog(null, timeDifferenceInHours);
            simpleTimer(timeDifferenceInHours);
            timer.start();
           
            if (DaysDiff < 0) {
                JOptionPane.showMessageDialog(null, "Invalid input!\nPlease check your inputted date", "Hotel Management System", JOptionPane.WARNING_MESSAGE);
                checkInDate.setDate(null);
                checkOutDate.setDate(null);
            }
            else {
                if (
                referenceNumber.getText().equals("") ||
                firstName.getText().equals("") ||
                lastName.getText().equals("") ||
                address.getText().equals("") || 
                contactNumber.getText().equals("") ||
                emailAddress.getText().equals("") ||
                referenceAddress.getText().equals("") || 
                birthDate.getDate() == null ||
                checkInDate.getDate() == null ||
                checkOutDate.getDate() == null
                    ) {
                    JOptionPane.showMessageDialog(null, "Please fill all the details", "Hotel Management System", JOptionPane.OK_OPTION);
                }
                else {
                    DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
                    model.addRow(new Object[] {
                        referenceNumber.getText(),
                        firstName.getText() + " " + lastName.getText(),
                        roomType.getSelectedItem(),
                        roomNumber.getSelectedItem(),
                        myDateFormat.format(checkInDate.getDate()),
                        myDateFormat.format(checkOutDate.getDate()),
                        timerLabel.getText()
                    });

                    int single = 1999;
                    int doubleRoom = 2999;
                    int guest = 3999;
                    int vip = 4999;

                    int oneMeal = 389;
                    int twoMeals = 799;
                    int threeMeals = 999;

                    if (roomType.getSelectedItem().equals("Single") && meals.getSelectedItem().equals("One meal")) {
                        totalSolver( single, oneMeal );
                    }
                    else if (roomType.getSelectedItem().equals("Single") && meals.getSelectedItem().equals("Two meals")) {
                        totalSolver( single, twoMeals );
                    }
                    else if (roomType.getSelectedItem().equals("Single") && meals.getSelectedItem().equals("Three meals")) {
                        totalSolver( single, threeMeals );
                    }
                    else if (roomType.getSelectedItem().equals("Double") && meals.getSelectedItem().equals("One meal")) {
                        totalSolver( doubleRoom, oneMeal );
                    }
                    else if (roomType.getSelectedItem().equals("Double") && meals.getSelectedItem().equals("Two meals")) {
                        totalSolver( doubleRoom, twoMeals );
                    }
                    else if (roomType.getSelectedItem().equals("Double") && meals.getSelectedItem().equals("Three meals")) {
                        totalSolver( doubleRoom, threeMeals );
                    }
                    else if (roomType.getSelectedItem().equals("Guest") && meals.getSelectedItem().equals("One meal")) {
                        totalSolver( guest, oneMeal );
                    }
                    else if (roomType.getSelectedItem().equals("Guest") && meals.getSelectedItem().equals("Two meals")) {
                        totalSolver( guest, twoMeals );
                    }
                    else if (roomType.getSelectedItem().equals("Guest") && meals.getSelectedItem().equals("Three meals")) {
                        totalSolver( guest, threeMeals );
                    }
                    else if (roomType.getSelectedItem().equals("VIP") && meals.getSelectedItem().equals("One meal")) {
                        totalSolver( vip, oneMeal );
                    }
                    else if (roomType.getSelectedItem().equals("VIP") && meals.getSelectedItem().equals("Two meals")) {
                        totalSolver( vip, twoMeals );
                    }
                    else if (roomType.getSelectedItem().equals("VIP") && meals.getSelectedItem().equals("Three meals")) {
                        totalSolver( vip, threeMeals );
                    }

                    //Updating the database
                    try {
                        connection = connector.getConnection();
                        pst = connection.prepareStatement("INSERT INTO hotel_management_system_main(reference_number, name, room_type, room_number, "
                                   + "address, reference_address, contact_number, email_address, gender, nationality, meals_per_day, check_in_date, "
                                   + "check_out_date, birth_date, Tax, Subtotal, Total, special_request, remaining_time) VALUES"
                                   + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                        pst.setString(1, referenceNumber.getText());
                        pst.setString(2, firstName.getText() + " " + lastName.getText());
                        pst.setString(3, roomType.getSelectedItem().toString());
                        pst.setString(4, roomNumber.getSelectedItem().toString());
                        pst.setString(5, address.getText());
                        pst.setString(6, referenceAddress.getText());
                        pst.setString(7, contactNumber.getText());
                        pst.setString(8, emailAddress.getText());
                        pst.setString(9, gender.getSelectedItem().toString());
                        pst.setString(10, nationality.getSelectedItem().toString());
                        pst.setString(11, meals.getSelectedItem().toString());                        
                        pst.setString(12, myDateFormat.format(checkInDate.getDate()));
                        pst.setString(13, myDateFormat.format(checkOutDate.getDate()));
                        pst.setString(14, myDateFormat.format(birthDate.getDate()));
                        pst.setString(15, tax.getText());
                        pst.setString(16, subtotal.getText());
                        pst.setString(17, total.getText());
                        pst.setString(18, specialRequest.getText());
                        pst.setString(19, String.valueOf(DaysDiff));
                        pst.execute();

                    } catch(ClassNotFoundException | SQLException e) {
                        JOptionPane.showMessageDialog(this, e);
                    } catch (Exception ex) {
                        Logger.getLogger(Final_Frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    roomNumberUpdate();
                    
                    referenceNumber.setText(null);
                    firstName.setText(null);
                    lastName.setText(null);
                    address.setText(null);
                    contactNumber.setText(null);
                    emailAddress.setText(null);
                    referenceAddress.setText(null);
                    nationality.setSelectedIndex(0);
                    gender.setSelectedIndex(0);
                    roomType.setSelectedIndex(0);
                    roomNumber.setSelectedIndex(0);
                    birthDate.setDate(null);
                    checkInDate.setDate(null);
                    checkOutDate.setDate(null);
                    meals.setSelectedIndex(0);
                    tax.setText(null);
                    subtotal.setText(null);
                    total.setText(null);
                    specialRequest.setText(null);

                    randomNumForReferenceNumber();
                    
                    JOptionPane.showMessageDialog(null, "Hotel booking update confirmed", "Hotel Management System", JOptionPane.OK_OPTION);
                }
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed
    private void roomNumberUpdate() {
        //Single
        if (roomType.getSelectedItem().equals("Single") && roomNumber.getSelectedItem().equals("101")) {
            singleRoom[0] = "101 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Single") && roomNumber.getSelectedItem().equals("102")) {
            singleRoom[1] = "102 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Single") && roomNumber.getSelectedItem().equals("103")) {
            singleRoom[2] = "103 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Single") && roomNumber.getSelectedItem().equals("104")) {
            singleRoom[3] = "104 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Single") && roomNumber.getSelectedItem().equals("105")) {
            singleRoom[4] = "105 (Occupied)";
        }
        //Double
        if (roomType.getSelectedItem().equals("Double") && roomNumber.getSelectedItem().equals("201")) {
            doubleRoom[0] = "201 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Double") && roomNumber.getSelectedItem().equals("202")) {
            doubleRoom[1] = "202 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Double") && roomNumber.getSelectedItem().equals("203")) {
            doubleRoom[2] = "203 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Double") && roomNumber.getSelectedItem().equals("204")) {
            doubleRoom[3] = "204 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Double") && roomNumber.getSelectedItem().equals("205")) {
            doubleRoom[4] = "205 (Occupied)";
        }
        //Guest
        if (roomType.getSelectedItem().equals("Guest") && roomNumber.getSelectedItem().equals("301")) {
            guestRoom[0] = "301 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Guest") && roomNumber.getSelectedItem().equals("302")) {
            guestRoom[1] = "302 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Guest") && roomNumber.getSelectedItem().equals("303")) {
            guestRoom[2] = "303 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Guest") && roomNumber.getSelectedItem().equals("304")) {
            guestRoom[3] = "304 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("Guest") && roomNumber.getSelectedItem().equals("305")) {
            guestRoom[4] = "305 (Occupied)";
        }
        //VIP
        if (roomType.getSelectedItem().equals("VIP") && roomNumber.getSelectedItem().equals("401")) {
            vipRoom[0] = "401 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("VIP") && roomNumber.getSelectedItem().equals("402")) {
            vipRoom[1] = "402 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("VIP") && roomNumber.getSelectedItem().equals("403")) {
            vipRoom[2] = "403 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("VIP") && roomNumber.getSelectedItem().equals("404")) {
            vipRoom[3] = "404 (Occupied)";
        }
        if (roomType.getSelectedItem().equals("VIP") && roomNumber.getSelectedItem().equals("405")) {
            vipRoom[4] = "405 (Occupied)";
        }
    }
    //Method for clear button
    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        randomNumForReferenceNumber();
        
        firstName.setText(null);
        lastName.setText(null);
        address.setText(null);
        referenceAddress.setText(null);
        contactNumber.setText(null);
        emailAddress.setText(null);
        nationality.setSelectedIndex(0);
        gender.setSelectedIndex(0);
        roomType.setSelectedIndex(0);
        roomNumber.setSelectedIndex(0);
        meals.setSelectedIndex(0);
        birthDate.setDate(null);
        checkInDate.setDate(null);
        checkOutDate.setDate(null);
        specialRequest.setText(null);
    }//GEN-LAST:event_clearActionPerformed

    private void referenceNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_referenceNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_referenceNumberActionPerformed
    //Method for table/sql connection
    private void mainTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainTableMouseClicked
        DefaultTableModel recordTable = (DefaultTableModel) mainTable.getModel(); // returns defaulttablemodel object
        int selectedRows = mainTable.getSelectedRow();
        
        //Spliting the "Name" into "First name" and "Last name"
        Scanner name = new Scanner(recordTable.getValueAt(selectedRows, 1).toString());
        StringBuilder tableFirstName = new StringBuilder();
        StringBuilder tableLastName = new StringBuilder();
        int count = 0;

        //Putting values in the jTextField relative to the selected row
        while (name.hasNext()) {
            count++;
            if (count == 1) tableFirstName.append(name.next());
            else if (count == 2) tableLastName.append(name.next()).append(" ");
        }
        referenceNumber.setText(recordTable.getValueAt(selectedRows, 0).toString());
        firstName.setText(tableFirstName.toString());
        lastName.setText(tableLastName.toString());
        roomType.setSelectedItem(recordTable.getValueAt(selectedRows, 2));
        roomNumber.setSelectedItem(recordTable.getValueAt(selectedRows, 3));
        
        try {
            pst = connection.prepareStatement("SELECT address, reference_address, contact_number, email_address, gender, nationality, birth_date, meals_per_day, check_in_date, check_out_date, Tax, Subtotal, Total FROM hotel_management_system_main WHERE reference_number = ?");
            
            pst.setString(1, String.valueOf(recordTable.getValueAt(selectedRows, 0)));
            resultSet = pst.executeQuery();
            while(resultSet.next()) {
                address.setText(resultSet.getString("address"));
                referenceAddress.setText(resultSet.getString("reference_address"));
                contactNumber.setText(resultSet.getString("contact_number"));
                emailAddress.setText(resultSet.getString("email_address"));
                gender.setSelectedItem(resultSet.getString("gender"));
                nationality.setSelectedItem(resultSet.getString("nationality"));
                birthDate.setDate(new Date(resultSet.getString("birth_date")));
                meals.setSelectedItem(resultSet.getString("meals_per_day"));
                checkInDate.setDate(new Date(resultSet.getString("check_in_date")));
                checkOutDate.setDate(new Date(resultSet.getString("check_out_date")));
                tax.setText(resultSet.getString("Tax"));
                subtotal.setText(resultSet.getString("Subtotal"));
                total.setText(resultSet.getString("Total"));
            }
            pdfTax = tax.getText();
            pdfSubtotal = subtotal.getText();
            pdfTotal = total.getText();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_mainTableMouseClicked

    private void taxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_taxActionPerformed
    //Method for ViewMore button
    private void viewMoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewMoreActionPerformed
        DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
        int selectedRow = mainTable.getSelectedRow();
        int numColumn;
        
        if (mainTable.getSelectedRow() != -1) {
            String Address = null;
            String ReferenceAddress = null;
            String ContactNumber = null;
            String EmailAddress = null;
            String Gender = null;
            String Nationality = null;
            String BirthDate = null;
            String ReferenceNumber = null;
            String Meals = null;
            String SpecialRequest = null;
        
            try {
                //Connecting to the database
                connection = connector.getConnection();

                //SQL statement
                pst = connection.prepareCall(""
                        + "SELECT address, reference_address, contact_number, email_address, gender, nationality, birth_date, reference_number, meals_per_day, special_request FROM hotel_management_system_main WHERE reference_number = ?");
                
                pst.setString(1, String.valueOf(model.getValueAt(selectedRow, 0)));

                //gets the data produced by the query
                resultSet = pst.executeQuery();

                //getting the Column Count
                ResultSetMetaData rsmData = resultSet.getMetaData();
                numColumn = rsmData.getColumnCount();

                //Inserting values into the row consecutively relative to the order of the column
                while(resultSet.next()) {
                    for (int i = 1; i <= numColumn; i++) {
                        Address  = resultSet.getString("address");
                        ReferenceAddress = resultSet.getString("reference_address");
                        ContactNumber = resultSet.getString("contact_number");
                        EmailAddress = resultSet.getString("email_address");
                        Gender = resultSet.getString("gender");
                        Nationality = resultSet.getString("nationality");
                        BirthDate = resultSet.getString("birth_date");
                        ReferenceNumber = resultSet.getString("reference_number");
                        Meals = resultSet.getString("meals_per_day");
                        SpecialRequest = resultSet.getString("special_request");
                    }
                }

            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            
            View_More viewmoreFrame = new View_More(Address, ReferenceAddress, ContactNumber, EmailAddress, Gender, Nationality, BirthDate, ReferenceNumber, Meals, SpecialRequest);
            viewmoreFrame.show();
        }
        else {
            JOptionPane.showMessageDialog(null, "Please select a row");
        }
        
        
    }//GEN-LAST:event_viewMoreActionPerformed

    private void roomNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomNumberActionPerformed

    }//GEN-LAST:event_roomNumberActionPerformed
    //Printing PDF
    private void printReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReceiptActionPerformed
        if (mainTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row", "Hotel Management System", JOptionPane.WARNING_MESSAGE);
        }
        else {
            DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
            int selectedRow = mainTable.getSelectedRow();
            int selectedReferenceNumber = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            try {
                PdfWriter.getInstance(doc, new FileOutputStream("C:\\Users\\Daniel\\Downloads\\" + selectedReferenceNumber + ".pdf"));
                doc.open();
                Paragraph paragraph1 = new Paragraph("                                                           Hotel Management System                                         \n\n");
                doc.add(paragraph1);
                Paragraph paragraph2 = new Paragraph("****************************************************************************************************************");
                doc.add(paragraph2);
                Paragraph paragraph3 = new Paragraph("Reference number: " + model.getValueAt(selectedRow, 0).toString() + "\nName: " +
                                                        model.getValueAt(selectedRow, 1).toString());
                doc.add(paragraph3);
                doc.add(paragraph2);
                Paragraph paragraph5 = new Paragraph("Room number: " + model.getValueAt(selectedRow, 3).toString() + "\nRoom type: " +
                                                        model.getValueAt(selectedRow, 2).toString() + "\nCheck In Date: " + 
                                                        model.getValueAt(selectedRow, 4).toString() + "\nCheck Out Date: " +
                                                        model.getValueAt(selectedRow, 5).toString());
                doc.add(paragraph5);
                doc.add(paragraph2);
                Paragraph paragraph7 = new Paragraph("Tax: " + pdfTax + "\nSubtotal: " + pdfSubtotal + "\nTotal: " + pdfTotal);
                doc.add(paragraph7);
                doc.add(paragraph2);
                Paragraph paragraph9 = new Paragraph("\n                                                          Thank you for having us!                                            ");
                doc.add(paragraph9);
                
            } catch(DocumentException | FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e);
            }
            doc.close();
            int answer = JOptionPane.showConfirmDialog(null, "Confirm selected action?", "Hotel Management System", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    if ((new File("C:\\Users\\Daniel\\Downloads\\" + selectedReferenceNumber + ".pdf")).exists()) {
                        Process process = Runtime
                                .getRuntime()
                                .exec("rundll32 url.dll, FileProtocolHandler C:\\Users\\Daniel\\Downloads\\" + selectedReferenceNumber + ".pdf");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "File does not exists.", "Hotel Management System", JOptionPane.WARNING_MESSAGE);
                    }
                } catch(IOException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }//GEN-LAST:event_printReceiptActionPerformed

    private void timerLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timerLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timerLabelActionPerformed
    //Method for total, subtotal, and tax
    void totalSolver( int typeOfRoom, int numberOfMeals ) {
        double total1 = (DaysDiff * typeOfRoom) + (numberOfMeals * DaysDiff);
            
        double computedSubtotal = total1;
        subtotal.setText(Double.toString(computedSubtotal));
            
        double computedTax = total1 * 0.08;
        tax.setText(Double.toString(computedTax));
            
        double computedTotal = computedSubtotal + computedTax;
        total.setText(Double.toString(computedTotal));
    }
    public static void main(String args[]) {        
        FlatDarkLaf.setup();
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Final_Frame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address;
    private com.toedter.calendar.JDateChooser birthDate;
    private com.toedter.calendar.JDateChooser checkInDate;
    private com.toedter.calendar.JDateChooser checkOutDate;
    private javax.swing.JButton clear;
    private javax.swing.JTextField contactNumber;
    private javax.swing.JTextField emailAddress;
    private javax.swing.JTextField firstName;
    private javax.swing.JComboBox<String> gender;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField lastName;
    private javax.swing.JLabel mainIcon;
    private javax.swing.JTable mainTable;
    private javax.swing.JComboBox<String> meals;
    private com.toedter.components.JLocaleChooser nationality;
    private javax.swing.JButton printReceipt;
    private javax.swing.JTextField referenceAddress;
    private javax.swing.JTextField referenceNumber;
    private javax.swing.JComboBox<String> roomNumber;
    private javax.swing.JComboBox<String> roomType;
    private javax.swing.JTextArea specialRequest;
    private javax.swing.JTextField subtotal;
    private javax.swing.JTextField tax;
    private javax.swing.JTextField timerLabel;
    private javax.swing.JTextField total;
    private javax.swing.JButton viewMore;
    // End of variables declaration//GEN-END:variables
}