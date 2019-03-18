/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edhome;

import funcoes.*;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author carlo
 */
public class Comandos implements CommandExecutor {

    private EDHome plugin;
    private String prefixo;

    public Comandos(EDHome plugin) {
        this.plugin = plugin;
        this.prefixo = plugin.getConfig().getString("Mensagens.prefixo").replaceAll("&", "§");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("ahome")) {
                if (args.length == 0) {

                    p.sendMessage("§f");
                    p.sendMessage("§f");
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_marcar").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_deletar").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_deletar_todas").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_teleportar").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_lista").replace("&", "§"));
                    p.sendMessage("§f");
                    p.sendMessage("§f");

                } else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        this.plugin.reloadConfig();
                        Conexao conexao = new Conexao(this.plugin);
                        conexao.abrirConexao();
                        conexao.criarTabela();
                        if (this.plugin.getConfig().getBoolean("Conexao.useMySQL") == true) {
                            p.sendMessage(this.prefixo + this.plugin.getConfig().getString("Mensagens.reload_sucessoMySQL").replace("&", "§"));
                        } else {
                            p.sendMessage(this.prefixo + this.plugin.getConfig().getString("Mensagens.reload_sucessoSQLite").replace("&", "§"));
                        }

                    }

                }
            }
            if (cmd.getName().equalsIgnoreCase("home")) {
                if (args.length == 0) {

                    p.sendMessage("§f");
                    p.sendMessage("§f");
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_marcar").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_deletar").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_deletar_todas").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_teleportar").replace("&", "§"));
                    p.sendMessage(this.plugin.getConfig().getString("Mensagens.home_lista").replace("&", "§"));
                    p.sendMessage("§f");
                    p.sendMessage("§f");

                } else {
                    if (args[0].equalsIgnoreCase("marcar") || args[0].equalsIgnoreCase("lista") || args[0].equalsIgnoreCase("deletar")) {
                        if (args[0].equalsIgnoreCase("marcar")) {
                            if (args.length >= 2) {

                                CriarHome home = new CriarHome(this.plugin);
                                int x = p.getLocation().getBlockX();
                                int y = p.getLocation().getBlockY();
                                int z = p.getLocation().getBlockZ();
                                String world = p.getLocation().getWorld().getName();
                                p.sendMessage(home.criarHome(p, args[1], x, y, z, world));

                            } else {
                                p.sendMessage(this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_marcar").replace("&", "§"));
                            }

                        }

                        if (args[0].equalsIgnoreCase("deletar")) {
                            if (args.length >= 2) {
                                if(args[1].equalsIgnoreCase("*")){
                                    DeletarHome home = new DeletarHome(this.plugin);
                                    p.sendMessage(home.deletarTodasAsHomes(p.getName()));
                                } else {
                                    DeletarHome home = new DeletarHome(this.plugin);
                                    p.sendMessage(home.deletarHome(p.getName(), args[1]));
                                }
                                
                                
                            } else {
                                p.sendMessage(this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_deletar").replace("&", "§"));
                            }
                        }

                        if (args[0].equalsIgnoreCase("lista")) {

                            ConsultarHome home = new ConsultarHome(this.plugin);
                            String m = null;
                            String[] teste = new String[6];
                            for(Object h : home.listarHome(p.getName())){
                                teste = h.toString().split(",");
                                if(m == null){
                                    m = teste[0].replaceAll("\\[", "");
                                } else {
                                    m += ", "+teste[0].replaceAll("\\[", "");
                                }
                                
                            }
                            if(m == null){
                                m = this.plugin.getConfig().getString("Mensagens.home_semhome").replace("&", "§");
                            }
                            p.sendMessage(this.prefixo + this.plugin.getConfig().getString("Mensagens.home_listar").replace("&", "§") + m);
                            
                            

                        }
                    } else {
                        if (args.length == 1) {
                            ConsultarHome home = new ConsultarHome(this.plugin);

                            if (home.consultarHome(p.getName(), args[0]) != null) {
                                ArrayList<String> homes = new ArrayList<>();
                                for (Object item : home.consultarHome(p.getName(), args[0])) {
                                    homes.add(item + "");
                                }
                                int x = Integer.parseInt(homes.get(1));
                                int y = Integer.parseInt(homes.get(2));
                                int z = Integer.parseInt(homes.get(3));
                                String world = homes.get(4);
                                p.teleport(new Location(Bukkit.getWorld(world), x, y, z));
                                p.sendMessage(this.prefixo + this.plugin.getConfig().getString("Mensagens.home_teleportado").replaceAll("&", "§").replaceAll("@argumento", homes.get(0)));
                            } else {
                                p.sendMessage(this.prefixo + this.plugin.getConfig().getString("Mensagens.erro_naoexiste").replaceAll("&", "§"));
                            }
                        }

                    }

                }

                return true;
            }

        }

        return false;
    }

}
