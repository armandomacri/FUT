package admin;

import mongo.MongoPlayerCard;
import neo4j.Neo4jPlayerCard;

import java.io.FileOutputStream;
import java.io.FileWriter;

public class UpdatePlayerCardsList {

    public boolean insertPlayerCards (String[] reader, String path){
        MongoPlayerCard mpc = new MongoPlayerCard();
        boolean mongoInsert = mpc.add(Integer.parseInt(reader[0]), reader[1], reader[2], reader[3], reader[4], reader[5],Integer.parseInt(reader[6]), reader[7], reader[8],
                                    reader[9], reader[10], reader[11], Integer.parseInt(reader[12]), Integer.parseInt(reader[13]), reader[14], Integer.parseInt(reader[15]),
                                    Integer.parseInt(reader[16]), Integer.parseInt(reader[17]), Integer.parseInt(reader[18]), Integer.parseInt(reader[19]),
                                    Integer.parseInt(reader[20]), reader[21], Integer.parseInt(reader[22]), Integer.parseInt(reader[23]), reader[24]);
        boolean neo4jInsert = false;
        if(mongoInsert){
            Neo4jPlayerCard n4pc = new Neo4jPlayerCard();
            neo4jInsert = n4pc.createPlayer(Integer.parseInt(reader[0]), reader[2], reader[3], reader[4], reader[24].split(",")[0]);
            if(neo4jInsert){
                return true;
            }
            else{
                mpc.deletePlayer(Integer.parseInt(reader[0]));
            }
        }
        try {
            FileWriter file = new FileWriter(path + "/notAdded.txt", true);
            file.write(reader[0]+"\n");
            file.close();
        } catch (Exception e) {

        }
        return false;
    }
}
