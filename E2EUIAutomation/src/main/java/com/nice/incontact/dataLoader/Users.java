package com.nice.incontact.dataLoader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Users extends BusinessUnit{
	public static String username = "";
    public static String userloginid = "";
    public static String userpassword = "";
    public static String userlastname = "";
    public static HashMap<String, Object> userMap;

    /**
     * This method will be used when multiple users need to be retrieved under
     * specific BU. E.g. Users.getUser(InputNode.userList.get(j)); where j
     * is index of ArrayList
     * 
     * @param userMap
     *            - Object of user map.
     */
    public static void getUser(Object userMap) {

        HashMap<String, Object> singleUserMap = null;

        singleUserMap = (HashMap<String, Object>) userMap;

        Set<String> keys = singleUserMap.keySet();
        for (String key : keys) {
            HashMap<String, Object> user = (HashMap<String, Object>) singleUserMap.get(key);
            username = (String) user.get("FirstName");
            userloginid = (String) user.get("Name");
            userpassword = (String) user.get("Password");
            userlastname = (String) user.get("LastName");
        }

    }

    /**
     * This method will return List of User Objects in ArrayList. E.g.
     * Users.getUser(InputNode.userList.get(j)); where j is index of ArrayList.
     * 
     * @param BUName
     *            - Name of BU node
     */
    public static ArrayList<Object> getUserList(String BUName) throws FileNotFoundException {
        ArrayList<Object> listofUsers = new ArrayList<Object>();
        try {

            Set<String> clusterSet = getBUData1(BUName).keySet();

            for (String key : clusterSet) {
                if (key.contains("User")) {
                    HashMap<String, Object> clusterKeyList = new HashMap<String, Object>();
                    clusterKeyList.put(key, getBUData1(BUName).get(key));
                    listofUsers.add(clusterKeyList);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return listofUsers;
    }

    /**
     * @param bu
     * @param userNode
     * @throws IllegalArgumentException
     *             if we cannot get the singleUserMap based on the userNode.
     */
    public static void getUser(String bu, String userNode) {
        HashMap<String, Object> buMap = getBUData(bu);
        HashMap<String, Object> singleUserMap = (HashMap<String, Object>) buMap.get(userNode);

        if (singleUserMap == null) {
            throw new IllegalArgumentException("The userNode '" + userNode + "' results in null data. Please check XML file.");
        } else {
            username = (String) singleUserMap.get("FirstName");
            userloginid = (String) singleUserMap.get("Name");
            userpassword = (String) singleUserMap.get("Password");
            userlastname = (String) singleUserMap.get("LastName");
        }
    }
}
