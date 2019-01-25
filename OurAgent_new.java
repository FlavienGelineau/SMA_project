package com.thales.mas.randomwalker;
import negotiator.Agent;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import java.util.Random;
import java.lang.Math;
import negotiator.session.Timeline;


/**@author CBU**/

public class RandomWalker extends Agent{
    private Random rand;
    private Bid lastBid;
    private Bid lastlastBid;
    private Bid lastlastlastBid;
    private Bid myBid;
    private Bid myLastBid; 

    private Double alpha;
    private Double beta;
    private Double gamma;

    private Double opponentUtility;
    private Bid nextBid;
    private Bid nextnextBid;


    /** Initialisation de l'agent */
    @Override
    public void init(){
        super.init();
        rand = new Random();
        alpha = 0.25;
        beta = 0.5;
        gamma = 15;
    }

    /** action accomplie par l'agent */
    @Override
    public Action chooseAction() {
        //Bidding strategy
        Bid bid = utilitySpace.getDomain().getRandomBid(rand);
        Action action = new Offer(getAgentID(), bid);
        Double t = timeline.getCurrentTime();
        Double T = timeline.getTotalTime();
        if(lastBid!=null) {
            //Acceptance strategy
            if (utilitySpace.getUtility(lastBid) > utilitySpace.getUtility(bid)) {
                return new Accept();
            }
            if (myLastBid!=null) {
                if (utilitySpace.getUtility(lastBid) > utilitySpace.getUtility(myLastBid)) { 
                    return new Accept();
                }
            }
            }
            if ((lastlastBid!=null) && (lastlastlastBid!=null)) {
                if (utilitySpace.getUtility(nextnextBid) < utilitySpace.getUtility(nextBid) < utilitySpace.getUtility(lastBid) < utilitySpace.getUtility(lastlastBid) < utilitySpace.getUtility(lastlastlastBid)) {
                    if (Math.random()<1/(1+exp(-alpha*(t-beta*T)/T))) { 
                        return new Accept();
                    }
                }  
            }
        }
        if (Math.random()<exp(gamma*(t-T)/T)) { 
                    return new Accept();
        }
        if (myBid!= Null){
            myLastBid = myBid;
        }
        myBid = bid;
        return action;
    }

    /** a la réception d'un message */
    @Override
    public void ReceiveMessage(Action action){
        if(action instanceof Offer)
            if (lastBid!=null) {
                if (lastlastBid!=null) {
                    lastlastlastBid = lastlastBid ; /* mis à jour de l'historique */
                }
                lastlastBid = lastBid ; /* mis à jour de l'historique */
            }
            lastBid = ((Offer) action).getBid();
    }
}
