from math import sqrt

def main(intersections):

    yPoints=[0.0, 1.0]

    for i in range(intersections-1):
        maxAreaIncreaseIndex=0
        maxAreaIncrease=0
        for j in range(len(yPoints)-1):

            xPoint=sqrt(1-pow(yPoints[j] , 2.0))
            neXPoint=sqrt(1-pow(yPoints[j+1] , 2.0))

            betweenYPoint=(yPoints[j+1]+yPoints[j])/2.0
            betweenXPoint=sqrt(1-pow(betweenYPoint , 2.0))
            
            oldArea=(yPoints[j+1]-yPoints[j])*(xPoint+neXPoint)/2.0

            newArea=(yPoints[j+1]-betweenYPoint)*(betweenXPoint+neXPoint)/2.0
            
            newArea=newArea+(betweenYPoint-yPoints[j])*(betweenXPoint+xPoint)/2.0

            if(newArea-oldArea>maxAreaIncrease):
                maxAreaIncrease=newArea-oldArea
                maxAreaIncreaseIndex=j


        yPoints.insert(maxAreaIncreaseIndex+1 , (yPoints[maxAreaIncreaseIndex]+yPoints[maxAreaIncreaseIndex+1])/2.0)

    piCalc=0;

    for i in range(len(yPoints)-1):
        xPoint=sqrt(1-pow(yPoints[i] , 2.0))
        neXPoint=sqrt(1-pow(yPoints[i+1] , 2.0))

        piCalc=piCalc+(xPoint+neXPoint)*(yPoints[i+1]-yPoints[i]);
        print(" "+str(yPoints[i]))
        

    print(2*piCalc)
            


if __name__ == '__main__':
    main(100)
