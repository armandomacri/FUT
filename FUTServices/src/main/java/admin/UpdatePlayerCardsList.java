package admin;

import mongo.MongoPlayerCard;
import neo4j.Neo4jPlayerCard;

public class UpdatePlayerCardsList {

    public boolean insertPlayerCards (String[] reader){
        MongoPlayerCard mpc = new MongoPlayerCard();
        int mongoInsert = mpc.add(Integer.parseInt(reader[0]), reader[1], reader[2], reader[3], reader[4], reader[5],Integer.parseInt(reader[6]), reader[7], reader[8],
                                    reader[9], reader[10], reader[11], Integer.parseInt(reader[12]), Integer.parseInt(reader[13]), reader[14], Integer.parseInt(reader[15]),
                                    Integer.parseInt(reader[16]), Integer.parseInt(reader[17]), Integer.parseInt(reader[18]), Integer.parseInt(reader[19]),
                                    Integer.parseInt(reader[20]), reader[21], Integer.parseInt(reader[22]), Integer.parseInt(reader[23]), reader[24]);
        boolean neo4jinsert = false;
        if(mongoInsert!=0){
            Neo4jPlayerCard n4pc = new Neo4jPlayerCard();
            neo4jinsert = n4pc.createPlayer(reader[0], reader[1]);
            if(neo4jinsert){
                return true;
            }
            else{
                mpc.deletePlayer(Integer.parseInt(reader[0]));
            }
        }
        return false;
    }
}