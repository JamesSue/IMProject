package online.green;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyFirstDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "online.green.dao");

        addNote(schema);

        new DaoGenerator().generateAll(schema, "F:\\android\\IMProject\\IMChiYu\\src\\main\\java-gen");


    }

    private static void addNote(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity msg = schema.addEntity("Msg");
        msg.addIdProperty();
        msg.addStringProperty("from").notNull();
        msg.addStringProperty("to").notNull();
        msg.addStringProperty("body");
        msg.addStringProperty("type");
        msg.addDateProperty("date");


    }
}
