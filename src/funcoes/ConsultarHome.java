/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funcoes;

import edhome.Conexao;
import edhome.EDHome;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.bukkit.Bukkit;

/**
 *
 * @author carlo
 */
public class ConsultarHome {

    private EDHome plugin;

    public ConsultarHome(EDHome plugin) {
        this.plugin = plugin;

    }

    public ArrayList listarHome(String player) {

        Conexao c = new Conexao(this.plugin);

        try {
            Connection conexao = c.abrirConexao();
            Statement st = conexao.createStatement();
            PreparedStatement p = conexao.prepareStatement("SELECT * FROM edhome WHERE usuario = ?");
            p.setString(1, player);
            ResultSet resultado = p.executeQuery();
            ArrayList<Object> lista = new ArrayList<>();
            while (resultado.next()) {

                ArrayList<String> dados = new ArrayList<>();
                dados.add(resultado.getString("home"));
                dados.add(resultado.getString("home_coordenadaX"));
                dados.add(resultado.getString("home_coordenadaY"));
                dados.add(resultado.getString("home_coordenadaZ"));
                dados.add(resultado.getString("home_mundo"));
                lista.add(dados);
            }

            conexao.close();

            return lista;

        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§c" + e);
        }
        return null;
    }

    public ArrayList consultarHome(String player, String home) {
        Conexao c = new Conexao(this.plugin);

        try {
            Connection conexao = c.abrirConexao();
            Statement st = conexao.createStatement();
            PreparedStatement p = conexao.prepareStatement("SELECT * FROM edhome WHERE usuario = ? AND home = ?");
            p.setString(1, player);
            p.setString(2, home);
            ResultSet resultado = p.executeQuery();
            if (resultado.next()) {
                ArrayList<String> dados = new ArrayList<>();
                dados.add(resultado.getString("home"));
                dados.add(resultado.getString("home_coordenadaX"));
                dados.add(resultado.getString("home_coordenadaY"));
                dados.add(resultado.getString("home_coordenadaZ"));
                dados.add(resultado.getString("home_mundo"));
                return dados;
            }
            conexao.close();

        } catch (Exception e) {
            System.out.println(e.toString());

        }

        return null;
    }

}
