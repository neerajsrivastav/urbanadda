//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2014 uniCenta & previous Openbravo POS works
//    http://www.unicenta.com
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.
package com.unicentaopos.pos.liquibase;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppViewConnection;
import com.openbravo.pos.util.AltEncrypter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class JResetLiquibase extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

   // private Statement stmt;

    private JResetLiquibase config;


    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppConfig config = new AppConfig(args);
                config.load();

                String db_user = (config.getProperty("db.user"));
                String db_url = (config.getProperty("db.URL"));
                String db_password = (config.getProperty("db.password"));

                if (db_user != null && db_password != null && db_password.startsWith("crypt:")) {
                    // the password is encrypted
                    AltEncrypter cypher = new AltEncrypter("cypherkey" + db_user);
                    db_password = cypher.decrypt(db_password.substring(6));
                }

                try {
                    Session session = AppViewConnection.createSession(config);
                    Connection con = DriverManager.getConnection(db_url, db_user, db_password);
                    Statement stmt = (Statement) con.createStatement();
                    String SQL = "DELETE FROM DATABASECHANGELOG ";
                    stmt.execute(SQL);
                    SQL = "UPDATE APPJL SET VERSION = '0.00' WHERE NAME ='uniCenta oPOS' ";
                    stmt.execute(SQL);
                    SQL = "UPDATE APPLICATIONS SET VERSION = '0.00' WHERE NAME ='uniCenta oPOS' ";
                    stmt.execute(SQL);                     
                    JOptionPane.showMessageDialog(null, "Liquibase tables cleared ready for new attempt.", "Liquibase Reset", JOptionPane.INFORMATION_MESSAGE);
                } catch (BasicException | SQLException e) {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(null, "Liquibase tables clear Failed.", "Liquibase Reset", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
