/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edhome;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author carlo
 */
public class EDHome extends JavaPlugin{

    @Override
    public void onEnable() {
        
        registrarComandos();
        saveDefaultConfig();
        Conexao conexao = new Conexao(this);
        conexao.criarTabela();
    }

    @Override
    public void onDisable() {
        this.saveDefaultConfig();
    }

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage("[EDHome] Plugin recarregado com sucesso");
    }

    /**
     * @param args the command line arguments
     */
    
    public void registrarComandos(){
        
        getCommand("home").setExecutor(new Comandos(this));
        getCommand("ahome").setExecutor(new Comandos(this));
    }
}
