package com.ez_mode;

import org.apache.logging.log4j.core.util.JsonUtils;

import javax.lang.model.element.NestingKind;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ProtoTest {
    private HashMap<String, Runnable> commands;
    private Map gameMap;
    private static ArrayList<String> args = new ArrayList<>();
    private boolean exited = false;
    private Scanner input = new Scanner(System.in);
    public ProtoTest(){
        commands = new HashMap<>();
        commands.put("print map", () -> MapPrintTest());
        commands.put("add character", () -> CharacterAddTest());
        commands.put("add node pump", () -> AddNewPumpTest());
        commands.put("add node pipe", () -> AddNewPipeTest());
        commands.put("character place pump", () -> PlacePumpTest());
        commands.put("character place pipe", () -> PlacePipeTest());
        commands.put("character move", () -> MoveCharacterTest());
        commands.put("character break", () -> BreakPipeTest());
        commands.put("character set", () -> SetPumpTest());
        commands.put("character repair pump", () -> RepairPumpTest());
        commands.put("character repair pipe", () -> RepairPipeTest());
        commands.put("character slippery", () -> MakePipeSlipperyTest());
        commands.put("character sticky", () -> MakePipeStickyTest());
        commands.put("exit", () -> exit());
        gameMap = new Map(5);
        gameMap.fillMap(2);
    }
    public void processCommand(){
        while(!exited) {
            args.clear();
            String cmd = input.nextLine();
            String[] tmp = cmd.split(" ");
            ArrayList<String> parts = new ArrayList<String>(Arrays.asList(tmp));
            for (String str : parts) {
                if (str.endsWith(">") && str.startsWith("<")) {
                    args.add(str);
                    parts.remove(str);
                }
            }
            cmd = String.join(" ", parts);
            if(commands.containsKey(cmd))
                commands.get(cmd).run();
            else System.out.println(cmd + "is an invalid command");
        }
    }
    public void MapPrintTest(){
        System.out.println("\tmap: ");
    }
    public void CharacterAddTest(){}
    public void AddNewPumpTest(){}
    public void AddNewPipeTest(){}
    public void PlacePumpTest(){}
    public void PlacePipeTest(){}
    public void MoveCharacterTest(){}
    public void BreakPipeTest(){}
    public void SetPumpTest(){}
    public void RepairPumpTest(){}
    public void RepairPipeTest(){}
    public void MakePipeSlipperyTest(){}
    public void MakePipeStickyTest(){}
    public void exit(){ exited = true;}
}
