/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edhome;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.bukkit.Bukkit;

/**
 *
 * @author carlo
 */
public class Conexao {

    private EDHome plugin;

    public Conexao(EDHome plugin) {
        this.plugin = plugin;
    }

    public Connection abrirConexao() {

        if (this.plugin.getConfig().getBoolean("Conexao.useMySQL") == true) {
            
            return abrirConexaoMySQL();

        } else {

            return abrirConexaoSQLite();

        }
    }

    public Connection abrirConexaoMySQL() {
        String URL = "jdbc:mysql://" + this.plugin.getConfig().getString("Conexao.Host") + ":" + this.plugin.getConfig().getString("Conexao.Porta") + "/" + this.plugin.getConfig().getString("Conexao.DataBase");
        try {
            Connection conexao = DriverManager.getConnection(URL, this.plugin.getConfig().getString("Conexao.Usuario"), this.plugin.getConfig().getString("Conexao.Senha"));
            return conexao;
        } catch (Exception e) {
            System.out.println("[EDHome] Houve um erro ao se conectar ao SQLite, desabilitando o plugin!");
            this.plugin.getPluginLoader().disablePlugin(this.plugin);
        }

        return null;
    }

    public Connection abrirConexaoSQLite() {
        String URL = "jdbc:sqlite:" + new File(this.plugin.getDataFolder() + "/database.db");
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conexao = DriverManager.getConnection(URL);
            return conexao;
        } catch (Exception e) {
            System.out.println("[EDHome] Houve um erro ao se conectar ao SQLite, desabilitando o plugin!");
            this.plugin.getPluginLoader().disablePlugin(this.plugin);
        }
        return null;
    }

    public boolean criarTabela() {
        Connection conexao = abrirConexao();
        if (conexao != null) {
            try {
                PreparedStatement prepare = conexao.prepareStatement(""
                        + "CREATE TABLE IF NOT EXISTS edhome("
                        + "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
                        + "usuario TEXT,"
                        + "home TEXT,"
                        + "home_coordenadaX TEXT,"
                        + "home_coordenadaY TEXT,"
                        + "home_coordenadaZ TEXT,"
                        + "home_mundo TEXT"
                        + ")");

                prepare.execute();
                conexao.close();
                return true;
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§c" + e + "");
                Bukkit.getConsoleSender().sendMessage(this.plugin.getConfig().getString("Mensagens.prefixo").replaceAll("&", "§") + " " + "§cHouve um erro ao criar a base de dados, desligando servidor!");
                Bukkit.shutdown();
                return false;
            }
        }
        Bukkit.getConsoleSender().sendMessage(this.plugin.getConfig().getString("Mensagens.prefixo").replaceAll("&", "§") + " " + "§cHouve um erro ao criar a base de dados, desligando servidor!");
        Bukkit.shutdown();
        return false;
    }

    

    
}
