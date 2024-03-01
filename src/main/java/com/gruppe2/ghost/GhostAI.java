package com.gruppe2.ghost;

import java.util.List;

public class GhostAI {


    public void ghostChase(){
        int x = 5;
        int y = 5;
    }

    public void ghostMove( List<Ghost> ghosts ){
        for(Ghost ghost : ghosts){
            ghost.chase();
        }
    }
}
