package com.ez_mode;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.*;
import com.ez_mode.objects.*;
import com.ez_mode.characters.*;

public class SkeletonTest {
    public void SetPumpTest(){
        Character character = new Plumber("Character");
        Pump pump = new Pump();
        Pipe absorber = new Pipe();
        Pipe source = new Pipe();
        try {
            pump.connect(absorber);
            pump.connect(source);
        }catch(ObjectFullException oe){
            System.out.println("A Node teli van");
        }
        character.placeTo(pump);
        character.SetPump();
    }
    public void PlumberMovesToPumpTest(){
        Plumber plumber = new Plumber("Plumber");
        Pipe pipe = new Pipe();
        Pump pump = new Pump();
        try {
            pipe.connect(pump);
        }catch(ObjectFullException oe){
            System.out.println(oe.getMessage());
        }
        plumber.placeTo(pipe);
        try{
            plumber.moveTo(pump);}
        catch(InvalidPlayerMovementException ipme){

            System.out.println(ipme.getMessage());
        }
        catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
    }
    public void PlumberMovesToPipeTest(){
        Plumber plumber = new Plumber("Plumber");
        Pipe pipe = new Pipe();
        Pump pump = new Pump();
        try {
            pipe.connect(pump);
        }catch(ObjectFullException oe){
            System.out.println(oe.getMessage());
        }
        plumber.placeTo(pump);
        try{
            plumber.moveTo(pipe);}
        catch(InvalidPlayerMovementException ipme){

            System.out.println(ipme.getMessage());
        }
        catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
    }
    public void NomadBreaksPipeTest(){
        Nomad nomad = new Nomad("Nomad");
        Pipe pipe = new Pipe();
        nomad.placeTo(pipe);
        nomad.breakNode();
    }
    public void NomadMovesToPipeTest(){
        Nomad nomad = new Nomad("Nomad");
        Pipe pipe = new Pipe();
        Pump pump = new Pump();
        try {
            pipe.connect(pump);
        }catch(ObjectFullException oe){
            System.out.println(oe.getMessage());
        }
        nomad.placeTo(pump);
        try{
            nomad.moveTo(pipe);}
        catch(InvalidPlayerMovementException ipme){

            System.out.println(ipme.getMessage());
        }
        catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
    }
    public void CharacterMovesTest(){
        Character character = new Nomad("Character");
        Node node1 = new Pump();
        Node node2 = new Pipe();
        try {
            node1.connect(node2);
        }catch(ObjectFullException oe){
            System.out.println(oe.getMessage());
        }
        character.placeTo(node1);
        try{
            character.moveTo(node2);}
        catch(InvalidPlayerMovementException ipme){

            System.out.println(ipme.getMessage());
        }
        catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
    }
    public void CharacterMovesToPumpTest(){
        Character character = new Plumber("Character");
        Pipe pipe = new Pipe();
        Pump pump = new Pump();
        try {
            pump.connect(pipe);
        }catch(ObjectFullException oe){
            System.out.println(oe.getMessage());
        }
        character.placeTo(pipe);
        try{
            character.moveTo(pump);}
        catch(InvalidPlayerMovementException ipme){

            System.out.println(ipme.getMessage());
        }
        catch(ObjectFullException ofe){
            System.out.println(ofe.getMessage());
        }
    }
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
