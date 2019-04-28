# chess_api

The source code is at the path:
    
    src/main/java/pl/art/lach/mateusz/javaopenchess

At this path, MySpringChess.java is theclass with main() method, that boots the whole application

##REST APIs for playing Chess
Following options are to be tried in Postman, when Spring application is launched

###Launch new game

Here, the parameter true means you (the client) would play as white, hence this api call would be followed by a call to "move"

    localhost:8080/new_game/true

    localhost:8080/new_game/false

###Perform move
    
    localhost:8080/move

json body:
    
    {
        "startSq":"e2",
        "endSq":"e4"
    }

To this, the call would reply in a json body of similar format, and the "message" variable would tell the status

###End the game

    localhost:8080/quit

##AWS Instance
- Public key
    ec2-3-82-92-129.compute-1.amazonaws.com

- username
    root
   
##Steps done
- Built spring application by separating UI from core logic
- run tests
- Tried to build osv on Windows, then Ubuntu inside Virtualbox, and finally on Ubuntu in dual-boot
- deployed osv to aws
- included in docker image

##Author
- Chinmay Gangal
- cganga2@uic.edu
