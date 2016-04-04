import random, sys, time, pygame
from pygame.locals import *

BROWN = (100 , 75 , 0)
DARK =  (30 , 30 , 30)

PIECEGREEN=(50,150,25)
PIECEBLUE=(100,150,200)

SIGNSIZE=7

WATER=[[0 for x in range(SIGNSIZE)] for x in range(SIGNSIZE)] # water symbol
WATER[0]=[0,0,0,0,0,0,0]
WATER[1]=[0,1,0,1,0,1,0]
WATER[2]=[1,0,1,0,1,0,1]
WATER[3]=[0,0,0,0,0,0,0]
WATER[4]=[0,1,0,1,0,1,0]
WATER[5]=[1,0,1,0,1,0,1]
WATER[6]=[0,0,0,0,0,0,0]

ANKH=[[0 for x in range(SIGNSIZE)] for x in range(SIGNSIZE)] #safe square symbol
ANKH[0]=[0,0,1,1,1,0,0]
ANKH[1]=[0,1,1,0,1,1,0]
ANKH[2]=[0,1,0,0,0,1,0]
ANKH[3]=[0,0,1,1,1,0,0]
ANKH[4]=[0,0,0,1,0,0,0]
ANKH[5]=[0,0,1,1,1,0,0]
ANKH[6]=[0,0,0,1,0,0,0]


BOARDWIDTH=10
BOARDHEIGHT=3
BOXSIZE=35
FPS = 15 # frames per second, the general speed of the program

def main():
    global FPSCLOCK, DISPLAYSURF
    pygame.init()
    DISPLAYSURF = pygame.display.set_mode((BOXSIZE*BOARDWIDTH, 200))
    FPSCLOCK = pygame.time.Clock()
    pygame.display.set_caption('SENET World!')
    mousex = 0 # used to store x coordinate of mouse event
    mousey = 0 # used to store y coordinate of mouse event

    dice=[2,2,2,2]
    boardPositions=[1,2,1,2,1,2,1,2,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    
    while True: # main game loop

        #handles both playerturns?
        while winner(boardPositions)==0:

            diceNumber=0;
            turnContinue=True

            drawBoard(boardPositions,dice)

            while turnContinue or diceNumber !=0:
                for event in pygame.event.get(): # event handling loop
                    if event.type == QUIT or (event.type == KEYUP and event.key == K_ESCAPE):
                        pygame.quit()
                        sys.exit()
                    elif event.type == MOUSEMOTION:
                        mousex, mousey = event.pos
                    elif event.type == MOUSEBUTTONUP:
                        mousex, mousey = event.pos
                        mouseClicked = True
                        if diceNumber!=0:
                            if mousey < BOARDHEIGHT*BOXSIZE:
                                boardIndex=int(mousex/BOXSIZE)+int(mousey/BOXSIZE)*BOARDWIDTH
                                if canMove(boardPositions, diceNumber, 1)!=0:
                                    diceNumber=updateBoard(boardPositions,boardIndex,diceNumber,1)
                                    drawBoard(boardPositions, dice)
                                   
                        
                        else:
                            disableDice(dice)
                            drawBoard(boardPositions, dice)
                            pygame.time.wait(250)
                            diceNumber=rollDice(dice)
                            drawBoard(boardPositions, dice)
                            if diceNumber==2 or diceNumber==3:
                                turnContinue=False
                            else:
                                turnContinue=True
    
            diceNumber=0;
            turnContinue=True
            while turnContinue or diceNumber !=0:
            
            
            #artificial loop
                disableDice(dice)
                drawBoard(boardPositions, dice)
                pygame.time.wait(250)
                diceNumber=rollDice(dice)
                drawBoard(boardPositions, dice)
                if diceNumber==2 or diceNumber==3:
                    turnContinue=False
                else:
                    turnContinue=True

                if canMove(boardPositions, diceNumber, 2)!=0:
                    diceNumber=AImakeMove(boardPositions, diceNumber, 2)

            #movepiece
            #dice update
            

#both for forward and backward positions
def updateBoard(boardPositions, boardIndex ,diceNumber, playerNumber):

    if canMovePiece(boardPositions, boardIndex ,diceNumber, playerNumber):
        if boardIndex+diceNumber>=len(boardPositions):
            boardPositions[boardIndex]=0
            return diceNumber-(len(boardPositions)-boardIndex) # return remaining diceNumber

        boardPositions[boardIndex]=boardPositions[boardIndex+diceNumber]
        boardPositions[boardIndex+diceNumber]=playerNumber
        return 0

    return diceNumber

def noRow(boardPositions, boardIndex ,diceNumber, playerNumber):

    countRow=0
    if diceNumber < 0:
        iterate=-1#temporary
        while iterate>=diceNumber:    
            if boardPositions[boardIndex+iterate]!=0 and boardPositions[boardIndex+iterate] !=playerNumber:
                countRow+=1
            else:
                countRow=0
            if countRow>=3:
                return False

            iterate-=1

        if countRow==2:
            return False

        if countRow==1 and (boardIndex+diceNumber)-1>0 and boardPositions[(boardIndex+diceNumber)-1]!=0 and boardPositions[(boardIndex+diceNumber)-1]!=playerNumber:
            return False
    else:
        if boardIndex+1>=len(boardPositions):
            return True

        
        iterate=1#temporary
        while iterate<=diceNumber:    
            if boardPositions[boardIndex+iterate]!=0 and boardPositions[boardIndex+iterate] !=playerNumber:
                countRow+=1
            else:
                countRow=0
            if countRow>=3:
                return False

            iterate+=1

            if boardIndex+iterate>=len(boardPositions):
                return True

        if countRow==2:
            return False

        if countRow==1 and (boardIndex+diceNumber)+1<=len(boardPositions) and boardPositions[(boardIndex+diceNumber)+1]!=0 and boardPositions[(boardIndex+diceNumber)+1]!=playerNumber:
            return False

    return True

def canMovePiece(boardPositions, boardIndex ,diceNumber, playerNumber):
    if boardIndex > len(boardPositions) or boardPositions[boardIndex]!=playerNumber or boardIndex+diceNumber<1:
        return False

    if boardIndex+diceNumber>=len(boardPositions) and noRow(boardPositions, boardIndex, diceNumber, playerNumber):
        return True # return remaining diceNumber

    if boardPositions[boardIndex+diceNumber]==playerNumber:
        return False

    if noRow(boardPositions, boardIndex, diceNumber, playerNumber):
        return True

    return False


def winner(boardpositions):
    p1PiecesLeft=False
    p2PiecesLeft=False
    
    for i in range(len(boardpositions)):
        if boardpositions[i]==1:
            p1PiecesLeft=True
        if boardpositions[i]==2:
            p2PiecesLeft=True
        if p1PiecesLeft and p2PiecesLeft:
            return 0

    if p1PiecesLeft:
        return 2
    
    return 1


def drawBoard(boardpositions , dice):
    DISPLAYSURF.fill(BROWN)
    
    for i in range(BOARDWIDTH):
        for j in range(BOARDHEIGHT):
            if ((i+j)%2==0):
                pygame.draw.rect(DISPLAYSURF,BROWN,(i*BOXSIZE,j*BOXSIZE,BOXSIZE,BOXSIZE))
            else:
                pygame.draw.rect(DISPLAYSURF,DARK,(i*BOXSIZE,j*BOXSIZE,BOXSIZE,BOXSIZE))

            drawSign(ANKH , PIECEBLUE, i, j)

    radius=int(BOXSIZE/2)
    for i in range(len(boardpositions)):
        if boardpositions[i]==1:
            pygame.draw.circle(DISPLAYSURF , PIECEGREEN , (i%10*BOXSIZE+radius , int(i/10)*BOXSIZE+radius) , radius , 0)
        if boardpositions[i]==2:
            pygame.draw.circle(DISPLAYSURF , PIECEBLUE , (i%10*BOXSIZE+radius , int(i/10)*BOXSIZE+radius) , radius , 0)                

    for i in range(len(dice)):
        COLOUR=PIECEBLUE
        if dice[i]==0:
            COLOUR=DARK
        elif dice[i]==1:
            COLOUR=BROWN
            
        pygame.draw.rect(DISPLAYSURF,COLOUR,(i*BOXSIZE, 5+(BOARDHEIGHT)*BOXSIZE,BOXSIZE,BOXSIZE))
    pygame.display.update()
    FPSCLOCK.tick(FPS) 

def drawSign(sign , COLOUR , boardx , boardy):

    scale=int(BOXSIZE/SIGNSIZE)

    for i in range(SIGNSIZE):
        for j in range(SIGNSIZE):
            if sign[j][i]==1:
             pygame.draw.rect(DISPLAYSURF,COLOUR,(boardx*BOXSIZE+i*scale , boardy*BOXSIZE+j*scale ,scale,scale))
    

def rollDice(dice):
    diceReturn=0;
    for i in range(len(dice)):
        dice[i]=random.getrandbits(1)
        diceReturn=diceReturn+dice[i]

    if diceReturn==0:
        diceReturn=6
        
    return diceReturn
    
def disableDice(dice):
    for i in range(len(dice)):
        dice[i]=2

def canMove(boardPositions, diceNumber, playerNumber):
    for i in range(len(boardPositions)):
        if boardPositions[i]==playerNumber:
            if canMovePiece(boardPositions, i ,diceNumber, playerNumber):
                return 1

    for i in range(len(boardPositions)):
        if boardPositions[i]==playerNumber:
            if canMovePiece(boardPositions, i ,-diceNumber, playerNumber):
                return -1

    return 0

##makes first possible move
def AImakeMove(boardPositions , diceNumber, playerNumber):
    for i in range(len(boardPositions)):
        if canMovePiece(boardPositions, i ,diceNumber, playerNumber):
            return updateBoard(boardPositions, i , diceNumber , playerNumber)

    for i in range(len(boardPositions)):
        if canMovePiece(boardPositions, i ,-diceNumber, playerNumber):
            return updateBoard(boardPositions, i ,-diceNumber, playerNumber)
        

if __name__ == '__main__':
    main()
