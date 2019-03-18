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
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author carlo
 */
public class CriarHome {

    private EDHome plugin;
    private String prefixo;

    public CriarHome(EDHome plugin) {
        this.plugin = plugin;
        this.prefixo = plugin.getConfig().getString("Mensagens.prefixo").replaceAll("&", "§");
    }

    public int contarHome(String player) {
        Conexao c = new Conexao(this.plugin);

        try {
            Connection conexao = c.abrirConexao();
            PreparedStatement prepare = conexao.prepareStatement("SELECT COUNT(home) FROM edhome WHERE usuario = ?");
            prepare.setString(1, player);
            ResultSet resultado = prepare.executeQuery();
            int r = 0;
            if(resultado.next()){
                r = resultado.getInt("COUNT(home)");
            }
            conexao.close();
            return r;

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return 0;
    }

    public String criarHome(Player player, String nome, int X, int Y, int Z, String world) {
        ConsultarHome home = new ConsultarHome(this.plugin);
        if (home.consultarHome(player.getName(), nome) != null) {

            return this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_jaexiste").replaceAll("&", "§");

        } else {

            Conexao c = new Conexao(this.plugin);
            
            int homeQuantidade = contarHome(player.getName());
            int homeLimit = this.plugin.getConfig().getInt("home_limits.default");
            
            if(player.hasPermission("EDHome.home.vip6")){
                
                homeLimit = this.plugin.getConfig().getInt("home_limits.vip6");
                
            } else if (player.hasPermission("EDHome.home.vip5")){
                
                homeLimit = this.plugin.getConfig().getInt("home_limits.vip5");
                
            } else if(player.hasPermission("EDHome.home.vip4")){
                
                homeLimit = this.plugin.getConfig().getInt("home_limits.vip4");
                
            }  else if(player.hasPermission("EDHome.home.vip3")){
                
                homeLimit = this.plugin.getConfig().getInt("home_limits.vip3");
                
            }  else if(player.hasPermission("EDHome.home.vip2")){
                
                homeLimit = this.plugin.getConfig().getInt("home_limits.vip2");
                
            }  else if(player.hasPermission("EDHome.home.vip1")){
                
                homeLimit = this.plugin.getConfig().getInt("home_limits.vip1");
                
            }
            if(homeQuantidade < homeLimit){
                try {
                    Connection conexao = c.abrirConexao();
                    PreparedStatement prepare = conexao.prepareStatement("INSERT INTO edhome "
                            + "(usuario, home, home_coordenadaX, home_coordenadaY, home_coordenadaZ, home_mundo)"
                            + "VALUES (?, ?, ?, ?, ?, ?)");
                    prepare.setString(1, player.getName());
                    prepare.setString(2, nome);
                    prepare.setString(3, X + "");
                    prepare.setString(4, Y + "");
                    prepare.setString(5, Z + "");
                    prepare.setString(6, world + "");
                    prepare.execute();
                    conexao.close();
                    return this.prefixo + this.plugin.getConfig().getString("Mensagens.home_criada").replaceAll("&", "§").replaceAll("@argumento", nome);
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("§c" + e + "");
                    Bukkit.getConsoleSender().sendMessage(this.prefixo + " " + "§cHouve um erro ao criar a home do jogador " + player + "!");
                    return this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_aocriar").replaceAll("&", "§");
                }
            } 
            return this.prefixo + this.plugin.getConfig().getString("Mensagens.excedeu_limite").replaceAll("&", "§").replaceAll("@limite", homeLimit+"");
            
        }

    }

}
