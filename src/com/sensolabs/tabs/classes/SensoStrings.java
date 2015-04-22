package com.sensolabs.tabs.classes;

public class SensoStrings {
	
	public static String PULSE_WAVEFORM = "waveform";
	public static String PULSE_PARAMETER = "parameter";
	public static String ECG_PARAMETER = "ECG_wave";
	public static String DEVICE_EASY_ECG = "PC80B";
	public static String DEVICE_PULSE_OXIMETER = "PC-60NW-1";
	public final static String DATA_RECEIVED_INTENT = "PULSE_OXIMETER";
	public final static String BYTE_RECEIVED_INTENT = "WAVE_PULSE_OXIMETER";
	public final static String CMD_FOR_PULSE_WAVEFORM = "";
	public final static String CMD_FOR_PULSE_PARAMETER = "";
	public final static String CMD_FOR_ECG_ACK = "";
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	
	
	/** For Easy ECG Monitors**/
    public static byte _command_ack_data[];
    public static byte _command_ack_set[];
    private static byte _command_head[];
    public  static byte _command_inquire[];
    private static byte _command_nak_data[];
    private static byte _command_nak_set[];
    private static byte _command_rej_data[];
    public static byte _command_rej_set[];
    private static byte _command_synctime[];
    public static byte crc_table[];
	
	  static 
	    {
	        byte abyte0[] = new byte[12];
	        abyte0[0] = -91;
	        abyte0[1] = 51;
	        abyte0[2] = 8;
	        _command_synctime = abyte0;
	        byte abyte1[] = new byte[5];
	        abyte1[0] = -91;
	        abyte1[1] = 85;
	        abyte1[2] = 1;
	        _command_ack_set = abyte1;
	        byte abyte2[] = new byte[5];
	        abyte2[0] = -91;
	        abyte2[1] = 85;
	        abyte2[2] = 1;
	        _command_nak_set = abyte2;
	        byte abyte3[] = new byte[5];
	        abyte3[0] = -91;
	        abyte3[1] = 85;
	        abyte3[2] = 1;
	        _command_rej_set = abyte3;
	        byte abyte4[] = new byte[6];
	        abyte4[0] = -91;
	        abyte4[1] = -86;
	        abyte4[2] = 2;
	        _command_ack_data = abyte4;
	        byte abyte5[] = new byte[6];
	        abyte5[0] = -91;
	        abyte5[1] = -86;
	        abyte5[2] = 2;
	        abyte5[4] = 1;
	        _command_nak_data = abyte5;
	        byte abyte6[] = new byte[6];
	        abyte6[0] = -91;
	        abyte6[1] = -86;
	        abyte6[2] = 2;
	        abyte6[4] = 2;
	        _command_rej_data = abyte6;
	        byte abyte7[] = new byte[7];
	        abyte7[0] = -91;
	        abyte7[1] = 17;
	        abyte7[2] = 3;
	        abyte7[3] = 1;
	        _command_inquire = abyte7;
	        byte abyte8[] = new byte[5];
	        abyte8[0] = -91;
	        abyte8[1] = -1;
	        abyte8[2] = 1;
	        _command_head = abyte8;
	        byte abyte9[] = new byte[256];
	        abyte9[1] = 94;
	        abyte9[2] = -68;
	        abyte9[3] = -30;
	        abyte9[4] = 97;
	        abyte9[5] = 63;
	        abyte9[6] = -35;
	        abyte9[7] = -125;
	        abyte9[8] = -62;
	        abyte9[9] = -100;
	        abyte9[10] = 126;
	        abyte9[11] = 32;
	        abyte9[12] = -93;
	        abyte9[13] = -3;
	        abyte9[14] = 31;
	        abyte9[15] = 65;
	        abyte9[16] = -99;
	        abyte9[17] = -61;
	        abyte9[18] = 33;
	        abyte9[19] = 127;
	        abyte9[20] = -4;
	        abyte9[21] = -94;
	        abyte9[22] = 64;
	        abyte9[23] = 30;
	        abyte9[24] = 95;
	        abyte9[25] = 1;
	        abyte9[26] = -29;
	        abyte9[27] = -67;
	        abyte9[28] = 62;
	        abyte9[29] = 96;
	        abyte9[30] = -126;
	        abyte9[31] = -36;
	        abyte9[32] = 35;
	        abyte9[33] = 125;
	        abyte9[34] = -97;
	        abyte9[35] = -63;
	        abyte9[36] = 66;
	        abyte9[37] = 28;
	        abyte9[38] = -2;
	        abyte9[39] = -96;
	        abyte9[40] = -31;
	        abyte9[41] = -65;
	        abyte9[42] = 93;
	        abyte9[43] = 3;
	        abyte9[44] = -128;
	        abyte9[45] = -34;
	        abyte9[46] = 60;
	        abyte9[47] = 98;
	        abyte9[48] = -66;
	        abyte9[49] = -32;
	        abyte9[50] = 2;
	        abyte9[51] = 92;
	        abyte9[52] = -33;
	        abyte9[53] = -127;
	        abyte9[54] = 99;
	        abyte9[55] = 61;
	        abyte9[56] = 124;
	        abyte9[57] = 34;
	        abyte9[58] = -64;
	        abyte9[59] = -98;
	        abyte9[60] = 29;
	        abyte9[61] = 67;
	        abyte9[62] = -95;
	        abyte9[63] = -1;
	        abyte9[64] = 70;
	        abyte9[65] = 24;
	        abyte9[66] = -6;
	        abyte9[67] = -92;
	        abyte9[68] = 39;
	        abyte9[69] = 121;
	        abyte9[70] = -101;
	        abyte9[71] = -59;
	        abyte9[72] = -124;
	        abyte9[73] = -38;
	        abyte9[74] = 56;
	        abyte9[75] = 102;
	        abyte9[76] = -27;
	        abyte9[77] = -69;
	        abyte9[78] = 89;
	        abyte9[79] = 7;
	        abyte9[80] = -37;
	        abyte9[81] = -123;
	        abyte9[82] = 103;
	        abyte9[83] = 57;
	        abyte9[84] = -70;
	        abyte9[85] = -28;
	        abyte9[86] = 6;
	        abyte9[87] = 88;
	        abyte9[88] = 25;
	        abyte9[89] = 71;
	        abyte9[90] = -91;
	        abyte9[91] = -5;
	        abyte9[92] = 120;
	        abyte9[93] = 38;
	        abyte9[94] = -60;
	        abyte9[95] = -102;
	        abyte9[96] = 101;
	        abyte9[97] = 59;
	        abyte9[98] = -39;
	        abyte9[99] = -121;
	        abyte9[100] = 4;
	        abyte9[101] = 90;
	        abyte9[102] = -72;
	        abyte9[103] = -26;
	        abyte9[104] = -89;
	        abyte9[105] = -7;
	        abyte9[106] = 27;
	        abyte9[107] = 69;
	        abyte9[108] = -58;
	        abyte9[109] = -104;
	        abyte9[110] = 122;
	        abyte9[111] = 36;
	        abyte9[112] = -8;
	        abyte9[113] = -90;
	        abyte9[114] = 68;
	        abyte9[115] = 26;
	        abyte9[116] = -103;
	        abyte9[117] = -57;
	        abyte9[118] = 37;
	        abyte9[119] = 123;
	        abyte9[120] = 58;
	        abyte9[121] = 100;
	        abyte9[122] = -122;
	        abyte9[123] = -40;
	        abyte9[124] = 91;
	        abyte9[125] = 5;
	        abyte9[126] = -25;
	        abyte9[127] = -71;
	        abyte9[128] = -116;
	        abyte9[129] = -46;
	        abyte9[130] = 48;
	        abyte9[131] = 110;
	        abyte9[132] = -19;
	        abyte9[133] = -77;
	        abyte9[134] = 81;
	        abyte9[135] = 15;
	        abyte9[136] = 78;
	        abyte9[137] = 16;
	        abyte9[138] = -14;
	        abyte9[139] = -84;
	        abyte9[140] = 47;
	        abyte9[141] = 113;
	        abyte9[142] = -109;
	        abyte9[143] = -51;
	        abyte9[144] = 17;
	        abyte9[145] = 79;
	        abyte9[146] = -83;
	        abyte9[147] = -13;
	        abyte9[148] = 112;
	        abyte9[149] = 46;
	        abyte9[150] = -52;
	        abyte9[151] = -110;
	        abyte9[152] = -45;
	        abyte9[153] = -115;
	        abyte9[154] = 111;
	        abyte9[155] = 49;
	        abyte9[156] = -78;
	        abyte9[157] = -20;
	        abyte9[158] = 14;
	        abyte9[159] = 80;
	        abyte9[160] = -81;
	        abyte9[161] = -15;
	        abyte9[162] = 19;
	        abyte9[163] = 77;
	        abyte9[164] = -50;
	        abyte9[165] = -112;
	        abyte9[166] = 114;
	        abyte9[167] = 44;
	        abyte9[168] = 109;
	        abyte9[169] = 51;
	        abyte9[170] = -47;
	        abyte9[171] = -113;
	        abyte9[172] = 12;
	        abyte9[173] = 82;
	        abyte9[174] = -80;
	        abyte9[175] = -18;
	        abyte9[176] = 50;
	        abyte9[177] = 108;
	        abyte9[178] = -114;
	        abyte9[179] = -48;
	        abyte9[180] = 83;
	        abyte9[181] = 13;
	        abyte9[182] = -17;
	        abyte9[183] = -79;
	        abyte9[184] = -16;
	        abyte9[185] = -82;
	        abyte9[186] = 76;
	        abyte9[187] = 18;
	        abyte9[188] = -111;
	        abyte9[189] = -49;
	        abyte9[190] = 45;
	        abyte9[191] = 115;
	        abyte9[192] = -54;
	        abyte9[193] = -108;
	        abyte9[194] = 118;
	        abyte9[195] = 40;
	        abyte9[196] = -85;
	        abyte9[197] = -11;
	        abyte9[198] = 23;
	        abyte9[199] = 73;
	        abyte9[200] = 8;
	        abyte9[201] = 86;
	        abyte9[202] = -76;
	        abyte9[203] = -22;
	        abyte9[204] = 105;
	        abyte9[205] = 55;
	        abyte9[206] = -43;
	        abyte9[207] = -117;
	        abyte9[208] = 87;
	        abyte9[209] = 9;
	        abyte9[210] = -21;
	        abyte9[211] = -75;
	        abyte9[212] = 54;
	        abyte9[213] = 104;
	        abyte9[214] = -118;
	        abyte9[215] = -44;
	        abyte9[216] = -107;
	        abyte9[217] = -53;
	        abyte9[218] = 41;
	        abyte9[219] = 119;
	        abyte9[220] = -12;
	        abyte9[221] = -86;
	        abyte9[222] = 72;
	        abyte9[223] = 22;
	        abyte9[224] = -23;
	        abyte9[225] = -73;
	        abyte9[226] = 85;
	        abyte9[227] = 11;
	        abyte9[228] = -120;
	        abyte9[229] = -42;
	        abyte9[230] = 52;
	        abyte9[231] = 106;
	        abyte9[232] = 43;
	        abyte9[233] = 117;
	        abyte9[234] = -105;
	        abyte9[235] = -55;
	        abyte9[236] = 74;
	        abyte9[237] = 20;
	        abyte9[238] = -10;
	        abyte9[239] = -88;
	        abyte9[240] = 116;
	        abyte9[241] = 42;
	        abyte9[242] = -56;
	        abyte9[243] = -106;
	        abyte9[244] = 21;
	        abyte9[245] = 75;
	        abyte9[246] = -87;
	        abyte9[247] = -9;
	        abyte9[248] = -74;
	        abyte9[249] = -24;
	        abyte9[250] = 10;
	        abyte9[251] = 84;
	        abyte9[252] = -41;
	        abyte9[253] = -119;
	        abyte9[254] = 107;
	        abyte9[255] = 53;
	        crc_table = abyte9;
	    }

}
