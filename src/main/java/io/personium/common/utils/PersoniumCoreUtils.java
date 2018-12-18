/**
 * personium.io
 * Copyright 2014-2018 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.personium.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 * Class that defines various utility functions.
 */
public final class PersoniumCoreUtils {

    /** Logger. */
    static Logger log = LoggerFactory.getLogger(PersoniumCoreUtils.class);

    private static final String AUTHZ_BASIC = "Basic ";
    private static final String AUTHZ_BEARER = "Bearer ";
    private static final int BITS_HEX_DIGIT = 4;
    private static final int HEX_DIGIT_MASK = 0x0F;
    private static final int CHARS_PREFIX_ODATA_DATE = 7;
    private static final int CHARS_SUFFIX_ODATA_DATE = 3;

    private static String fqdn = null;

    private PersoniumCoreUtils() {
    }

    /**
     * Set FQDN.
     * @param fqdnValue FQDN of this unit
     */
    public static void setFQDN(String fqdnValue) {
        if (fqdn == null) {
            fqdn = fqdnValue;
        }
    }

    /**
     * Get FQDN.
     * @return FQDN of this unit.
     */
    public static String getFQDN() {
      return fqdn;
    }

    /**
     * Personium's http headers.
     */
    public static class HttpHeaders {
        /** Accountのパスワード設定・変更を受け付けるヘッダ. */
        public static final String X_PERSONIUM_CREDENTIAL = "X-Personium-Credential";
        /**
         * X-Personium-Unit-Userヘッダ.
         * MasterTokenでのアクセス時に、このヘッダがある場合は、
         * ヘッダ値で指定された任意のユニットユーザとして振る舞う。
         */
        public static final String X_PERSONIUM_UNIT_USER = "X-Personium-Unit-User";
        /** Depthヘッダ. */
        public static final String DEPTH = "Depth";
        /** X-HTTP-Method-Overrideヘッダ. */
        public static final String X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";
        /** X-Overrideヘッダ. */
        public static final String X_OVERRIDE = "X-Override";
        /** X-Forwarded-Protoヘッダ. */
        public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
        /** X-Forwarded-Hostヘッダ. */
        public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
        /** X-Forwarded-Pathヘッダ. */
        public static final String X_FORWARDED_PATH = "X-Forwarded-Path";
        /** X-Personium-Unit-Hostヘッダ. */
        public static final String X_PERSONIUM_UNIT_HOST = "X-Personium-Unit-Host";
        /** X-Personium-Versionヘッダ. */
        public static final String X_PERSONIUM_VERSION = "X-Personium-Version";
        /** X-Personium-Recursiveヘッダ. */
        public static final String X_PERSONIUM_RECURSIVE = "X-Personium-Recursive";
        /** X-Personium-RequestKeyヘッダ. */
        public static final String X_PERSONIUM_REQUESTKEY = "X-Personium-RequestKey";
        /** X-Personium-EventId header. */
        public static final String X_PERSONIUM_EVENTID = "X-Personium-EventId";
        /** X-Personium-RuleChain header. */
        public static final String X_PERSONIUM_RULECHAIN = "X-Personium-RuleChain";
        /** X-Personium-Via header. */
        public static final String X_PERSONIUM_VIA = "X-Personium-Via";
        // CORS
        /** Access-Control-Allow-Origin. */
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        /** Access-Control-Allow-Headers. */
        public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
        /** Access-Control-Request-Headers. */
        public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
        /** Access-Control-Allow-Methods. */
        public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        /** Access-Control-Expose-Headers. */
        public static final String ACCESS_CONTROLE_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
        /** Access-Control-Allow-Credentials. */
        public static final String ACCESS_CONTROLE_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
        /** Access-Control-Max-Age. */
        public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

        /** Originヘッダ. */
        public static final String ORIGIN = "Origin";
        /** Allowヘッダ. */
        public static final String ALLOW = "Allow";
        /** Rangeヘッダ. */
        public static final String RANGE = "Range";
        /** Accept-Rangeヘッダ. */
        public static final String ACCEPT_RANGES = "Accept-Ranges";
        /** Content-Rangeヘッダ. */
        public static final String CONTENT_RANGE = "Content-Range";

        /**
         * 典型的なヘッダ値.
         */
        public static class Value {
            /**
             * *
             */
            public static final String ASTERISK = "*";
        }
    }

    /**
     * Definition of http methods.
     */
    public static class HttpMethod {
        /** MERGE. */
        public static final String MERGE = "MERGE";
        /** MKCOL. */
        public static final String MKCOL = "MKCOL";
        /** PROPFIND. */
        public static final String PROPFIND = "PROPFIND";
        /** PROPPATCH. */
        public static final String PROPPATCH = "PROPPATCH";
        /** ACL. */
        public static final String ACL = "ACL";
        /** COPY. */
        public static final String COPY = "COPY";
        /** MOVE. */
        public static final String MOVE = "MOVE";
        /** LOCK. */
        public static final String LOCK = "LOCK";
        /** UNLOCK. */
        public static final String UNLOCK = "UNLOCK";
    }

    /**
     * Constant definition for XML.
     */
    public static class XmlConst {
        /** urn:x-personium:xmlns. */
        public static final String NS_PERSONIUM = "urn:x-personium:xmlns";
        /** XML Name Space p:. */
        public static final String NS_PREFIX_PERSONIUM = "p";

        /** service. */
        public static final String SERVICE = "service";
        /** odata. */
        public static final String ODATA = "odata";
        /** stream collection type. */
        public static final String STREAM = "stream";

        /** cellstatus. */
        public static final String CELL_STATUS = "cellstatus";
    }

    /**
     * Constant definition for ContentType.
     */
    public static class ContentType {
        /** application/zip+x-personium-bar. */
        public static final String CONTENT_TYPE_BAR = "application/zip+x-personium-bar";
    }

    /**
     * XMLのDOMノードを文字列に変換します.
     * @param node 文字列化したいノード
     * @return 変換結果の文字列
     */
    public static String nodeToString(final Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            throw new RuntimeException("nodeToString Transformer Exception", te);
        }
        return sw.toString();
    }

    /**
     * プログラムリソース中のファイルをStringとして読み出します.
     * @param resPath リソースパス
     * @param encoding Encoding
     * @return 読み出した文字列
     */
    public static String readStringResource(final String resPath, final String encoding) {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = PersoniumCoreUtils.class.getClassLoader().getResourceAsStream(resPath);
            br = new BufferedReader(new InputStreamReader(is, encoding));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Base64urlのエンコードを行う.
     * 厳密にはRFC４648参照。（といいたいが、少し表現があいまい。） ---------------------------------------------------
     * http://tools.ietf.org/html/rfc4648 --------------------------------------------------- 5. Base 64 Encoding with
     * URL and Filename Safe Alphabet The Base 64 encoding with an URL and filename safe alphabet has been used in [12].
     * ＵＲＬとファイル名で安全なアルファベットのベース６４符号化は[12]で 使われました。 An alternative alphabet has been suggested that would use "~" as the
     * 63rd character. Since the "~" character has special meaning in some file system environments, the encoding
     * described in this section is recommended instead. The remaining unreserved URI character is ".", but some file
     * system environments do not permit multiple "." in a filename, thus making the "." character unattractive as well.
     * ６３番目のアルファベットの代わりの文字として"~"を使うのが示唆され ました。"~"文字があるファイルシステム環境で特別な意味を持つので、 この章で記述された符号化はその代わりとして勧められます。残りの予
     * 約なしのＵＲＩ文字は"."ですが、あるファイルシステム環境で複数の"." は許されず、"."文字も魅力的でありません。 The pad character "=" is typically percent-encoded
     * when used in an URI [9], but if the data length is known implicitly, this can be avoided by skipping the padding;
     * see section 3.2. ＵＲＩ[9]で使われるとき、穴埋め文字"="は一般にパーセント符号化さ れますが、もしデータ長が暗黙のうちにわかるなら、穴埋めをスキップす
     * ることによってこれを避けれます；３.２章を見て下さい。 This encoding may be referred to as "base64url". This encoding should not be regarded
     * as the same as the "base64" encoding and should not be referred to as only "base64". Unless clarified otherwise,
     * "base64" refers to the base 64 in the previous section. この符号化は"base64url"と述べられるかもしれません。この符号化は
     * "base64"符号化と同じと見なされるべきではなくて、単純に「ベース６ ４」と述べるべきではありません。他に明示されない限り、"base64"が 前章のベース６４を意味します。 This encoding is
     * technically identical to the previous one, except for the 62:nd and 63:rd alphabet character, as indicated in
     * Table 2. この符号化は、６２番目と６３番目のアルファベット文字が表２で示さ れたものである以外は、技術的に前のものとまったく同じです。 Table 2: The "URL and Filename safe" Base
     * 64 Alphabet 表２：「ＵＲＬとファイル名で安全な」ベース６４アルファベット Value Encoding Value Encoding Value Encoding Value Encoding 0 A 17 R
     * 34 i 51 z 1 B 18 S 35 j 52 0 2 C 19 T 36 k 53 1 3 D 20 U 37 l 54 2 4 E 21 V 38 m 55 3 5 F 22 W 39 n 56 4 6 G 23 X
     * 40 o 57 5 7 H 24 Y 41 p 58 6 8 I 25 Z 42 q 59 7 9 J 26 a 43 r 60 8 10 K 27 b 44 s 61 9 11 L 28 c 45 t 62 -
     * (minus) 12 M 29 d 46 u 63 _ 13 N 30 e 47 v (underline) 14 O 31 f 48 w 15 P 32 g 49 x 16 Q 33 h 50 y (pad) =
     * @param in エンコードしたいbyte列
     * @return エンコードされたあとの文字列
     */
    public static String encodeBase64Url(final byte[] in) {
        return Base64.encodeBase64URLSafeString(in);
    }

    /**
     * Base64urlのエンコードを行う.
     * @param inStr 入力ストリーム
     * @return 文字列
     */
    public static String encodeBase64Url(final InputStream inStr) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int l = 0;
        try {
            while ((l = inStr.read()) != -1) {
                baos.write(l);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeBase64URLSafeString(baos.toByteArray());
    }

    /**
     * Base64urlのデコードを行う.
     * @param in デコードしたい文字列
     * @return デコードされたbyte列
     */
    public static byte[] decodeBase64Url(final String in) {
        return Base64.decodeBase64(in);
    }

    /**
     * バイト列を16進数の文字列に変換する. TODO さすがにこんなのどこかにライブラリありそうだけど.
     * @param input 入力バイト列
     * @return 16進数文字列
     */
    public static String byteArray2HexString(final byte[] input) {
        StringBuffer buff = new StringBuffer();
        int count = input.length;
        for (int i = 0; i < count; i++) {
            buff.append(Integer.toHexString((input[i] >> BITS_HEX_DIGIT) & HEX_DIGIT_MASK));
            buff.append(Integer.toHexString(input[i] & HEX_DIGIT_MASK));
        }
        return buff.toString();
    }

    /**
     * URLエンコードのエンコードを行う.
     * @param in Urlエンコードしたい文字列
     * @return Urlエンコードされた文字列
     */
    public static String encodeUrlComp(final String in) {
        try {
            return URLEncoder.encode(in, CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * URLエンコードのデコードを行う.
     * @param in Urlエンコードされた文字列
     * @return 元の文字列
     */
    public static String decodeUrlComp(final String in) {
        try {
            return URLDecoder.decode(in, CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * InputStreamをすべて読み、String型で返す.
     * @param is InputStream
     * @return 文字列
     */
    public static String readInputStreamAsString(InputStream is) {

        InputStreamReader isr = null;
        BufferedReader reader = null;
        String bodyString = null;
        try {

            isr = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            int chr;
            while ((chr = is.read()) != -1) {
                sb.append((char) chr);
            }
            bodyString = sb.toString();
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            log.info(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        return bodyString;
    }

    /**
     * Authorizationヘッダの内容をBasic認証のものとしてパースする.
     * @param authzHeaderValue Authorizationヘッダの内容
     * @return id, pwの２要素の文字列配列、またはパース失敗時はnull
     */
    public static String[] parseBasicAuthzHeader(String authzHeaderValue) {
        if (authzHeaderValue == null || !authzHeaderValue.startsWith(AUTHZ_BASIC)) {
            return null;
        }
        try {
            // 認証スキーマ以外の部分を取得
            byte[] bytes = PersoniumCoreUtils.decodeBase64Url(authzHeaderValue.substring(AUTHZ_BASIC.length()));
            String rawStr = new String(bytes, CharEncoding.UTF_8);
            int pos = rawStr.indexOf(":");
            // 認証トークンの値に「:」を含んでいない場合は認証エラーとする
            if (pos == -1) {
                return null;
            }
            String username = rawStr.substring(0, pos);
            String password = rawStr.substring(pos + 1);
            return new String[] {decodeUrlComp(username), decodeUrlComp(password) };
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * basic認証ヘッダを生成して返します.
     * @param id id
     * @param pw pw
     * @return basic認証のヘッダ
     */
    public static String createBasicAuthzHeader(final String id, final String pw) {
        String line = encodeUrlComp(id) + ":" + encodeUrlComp(pw);
        try {
            return encodeBase64Url(line.getBytes(CharEncoding.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse Authorization header as Bearer.
     * @param authzHeaderValue Authorization Header
     * @return token
     */
    public static String parseBearerAuthzHeader(String authzHeaderValue) {
        if (authzHeaderValue == null || !authzHeaderValue.startsWith(AUTHZ_BEARER)) {
            return null;
        }
        return authzHeaderValue.substring(AUTHZ_BEARER.length());
    }

    /**
     * Create Bearer Authorization header.
     * @param token token string
     * @return bearer authorization header
     */
    public static String createBearerAuthzHeader(final String token) {
        return AUTHZ_BEARER + token;
    }

    /**
     * ODataのDatetime JSONリテラル(Date(\/ ... \/) 形式)を解釈してDateオブジェクトに変換します.
     * @param odataDatetime ODataのDatetime JSONリテラル
     * @return Dateオブジェクト
     */
    public static Date parseODataDatetime(final String odataDatetime) {
        String dateValue = odataDatetime
                .substring(CHARS_PREFIX_ODATA_DATE, odataDatetime.length() - CHARS_SUFFIX_ODATA_DATE);
        return new Date(Long.valueOf(dateValue));
    }
}
