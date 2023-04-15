package com.ez_mode;

import com.ez_mode.characters.*;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.*;

public class SkeletonTest {
    public void SetPumpTest(){

    }
    public void PlumberMovesToPumpTest(){}
    public void PlumberMovesToPipeTest(){}
    public void NomadBreaksPipeTest(){
        Nomad nomad = new Nomad("Nomad");
        Pipe pipe = new Pipe();
    }
    public void NomadMovesToPipeTest(){}
    public void CharacterMovesTest(){}
    public void CharacterMovesToPumpTest(){}
    public void PlumberRepairsPipeTest(){
        Pipe pipe= new Pipe();
        try {
            pipe.breakNode(new Nomad("Testnomad"));
        } catch (InvalidPlayerActionException e) {
            System.out.println(e.getMessage());
        }
        Plumber p1= new Plumber("Testplumber");
        p1.placeTo(pipe);
        p1.repair();}
    public void PlumberRepairsPumpTest(){
        Pump pumpa= new Pump();
        try {
            pumpa.breakNode(new Nomad("Testnomad"));
        } catch (InvalidPlayerActionException e) {
            System.out.println(e.getMessage());
        }
        Plumber p1= new Plumber("Testplumber");
        p1.placeTo(pumpa);
        p1.repair();

    }
    public void PlumberPicksUpPipe(){
        Cistern c1= new Cistern();
        Plumber p1= new Plumber("Plumber1");
        p1.placeTo(c1);
        c1.MakePipe();
        p1.PickupPipe();
    }
    public void PlumberPicksUpPump(){
        Cistern c1= new Cistern();
        Plumber p1= new Plumber("Plumber1");
        p1.placeTo(c1);
        c1.MakePump();
        p1.PickupPump();
    }
    public void PlumberDeploysPump(){
        Cistern c1= new Cistern();
        Plumber p1= new Plumber("Plumber1");
        p1.placeTo(c1);
        c1.MakePump();
        p1.PickupPump();
        Pipe Pipe1=new Pipe();
        try{
            Pipe1.connect(c1);
        }catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
        p1.placeTo(Pipe1);
        try{
            p1.moveTo((Node)c1);}
        catch(InvalidPlayerMovementException ipme){

            System.out.println(ipme.getMessage());
        }
        catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        };
        p1.PlacePump();

    }
    public void PlumberMovesToCistern() {
        Cistern c1=new Cistern();
        Pipe Pipe1=new Pipe();
        Plumber p1=new Plumber("Plumber1");
        try {
            Pipe1.connect(c1);
        }catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
        p1.placeTo(Pipe1);
        try{
        p1.moveTo((Node)c1);}
        catch(InvalidPlayerMovementException ipme){

            System.out.println(ipme.getMessage());
        }
        catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
    }
}
