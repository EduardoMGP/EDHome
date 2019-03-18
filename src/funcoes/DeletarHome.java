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
import org.bukkit.Bukkit;

/**
 *
 * @author carlo
 */
public class DeletarHome {

    private EDHome plugin;
    private String prefixo;
    
    public DeletarHome(EDHome plugin) {
        this.plugin = plugin;
        this.prefixo = plugin.getConfig().getString("Mensagens.prefixo").replaceAll("&", "§");
    }
    
    public String deletarHome(String player, String nome) {

        ConsultarHome home = new ConsultarHome(this.plugin);
        if (home.consultarHome(player, nome) == null) {

            return this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_naoexiste").replaceAll("&","§");
            
        } else {
            
            Conexao c = new Conexao(this.plugin);
            
            
            try {
                Connection conexao = c.abrirConexao();
                PreparedStatement prepare = conexao.prepareStatement("DELETE FROM edhome WHERE usuario = ? AND home = ?");
                prepare.setString(1, player);
                prepare.setString(2, nome);
                prepare.execute();
                conexao.close();
                return this.prefixo + this.plugin.getConfig().getString("Mensagens.home_deletada").replaceAll("&","§").replaceAll("@argumento", nome);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§c" + e + "");
                Bukkit.getConsoleSender().sendMessage(this.prefixo + " " + "§cHouve um erro ao deletar a home do jogador "+player+"!");
                return this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_aodeletar").replaceAll("&","§");
            }
        }

        
    }
    
    public String deletarTodasAsHomes(String player) {

        ConsultarHome home = new ConsultarHome(this.plugin);
        if (home.listarHome(player).isEmpty()) {

            return this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_naopossuihome").replaceAll("&","§");
            
        } else {
            
            Conexao c = new Conexao(this.plugin);
            
            
            
            try {
                Connection conexao = c.abrirConexao();
                PreparedStatement prepare = conexao.prepareStatement("DELETE FROM edhome WHERE usuario = ?");
                prepare.setString(1, player);
                prepare.execute();
                conexao.close();
                return this.prefixo + this.plugin.getConfig().getString("Mensagens.home_deletadaAll").replaceAll("&","§");
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§c" + e + "");
                Bukkit.getConsoleSender().sendMessage(this.prefixo + " " + "§cHouve um erro ao deletar as homes do jogador "+player+"!");
                return this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_aodeletar").replaceAll("&","§");
            }
        }

        
    }
}
