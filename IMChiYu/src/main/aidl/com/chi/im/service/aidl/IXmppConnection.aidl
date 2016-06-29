// IXmppConnection.aidl
package com.chi.im.service.aidl;

// Declare any non-default types here with import statements

interface IXmppConnection {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
            /**建立连接****/
            void connect();
            /**登录****/
            void login();
            /**关闭连接****/
            void disconnect();




}
