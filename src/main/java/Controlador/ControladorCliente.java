package Controlador;

import Configuracion.Conexion;
import Modelo.Cliente;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Emilio Mayer
 */
public class ControladorCliente {

    Conexion conexion = new Conexion();
    Cliente cliente = new Cliente();

    //Trae todos los clientes
    public void mostrarClientes(JTable tablaTotalClientes) {

        //objeto para modelar la tabla
        DefaultTableModel modelo = new DefaultTableModel() {
            //Inhabilita la edicion de la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Variable para la sentencia de sql
        String sql = "";

        //Agregamos los titulos de la tabla
        modelo.addColumn("id");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellido");
        modelo.addColumn("DNI");
        modelo.addColumn("Telefono");
        modelo.addColumn("email");

        //Asignamos el modelo a la tabla que recibe la funcion por parametro
        tablaTotalClientes.setModel(modelo);

        //Sentencia sql
        sql = "SELECT cliente.idCliente, cliente.nombres, cliente.apellido, cliente.dni, cliente.telefono, cliente.email FROM cliente";

        try {
            //Establece conexion con bd y ejecuta la consulta sql
            Statement st = conexion.establecerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                //Obtiene los datos de la bd para añadirlos posteriormente al modelo
                cliente.setIdCliente(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("nombres"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getString("dni"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));

                //Añade un registro la modelo
                modelo.addRow(new Object[]{cliente.getIdCliente(), cliente.getNombre(),
                    cliente.getApellido(), cliente.getDni(), cliente.getTelefono(), cliente.getEmail()});
            }

            //Se le asigna el modelo con los titulos y contenido de la bd a la tabla
            tablaTotalClientes.setModel(modelo);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al mostrar clientes: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    //Agregar Cliente
    public void AgregarCliente(JTextField nombres, JTextField apellido, JTextField dni, JTextField telefono, JTextField email) {

        String sql = "INSERT INTO cliente (nombres, apellido, dni, telefono, email) VALUES (?,?,?,?,?)";

        try {
            cliente.setNombre(nombres.getText());
            cliente.setApellido(apellido.getText());
            cliente.setDni(dni.getText());
            cliente.setTelefono(telefono.getText());
            cliente.setEmail(email.getText());

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de registrar los datos del cliente?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);
                cs.setString(1, cliente.getNombre());
                cs.setString(2, cliente.getApellido());
                cs.setString(3, cliente.getDni());
                cs.setString(4, cliente.getTelefono());
                cs.setString(5, cliente.getEmail());

                cs.execute();

                JOptionPane.showMessageDialog(null, "El nuevo cliente fue registrado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Se cancelo guardar cliente");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar nuevo cliente: " + ex.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    public void modificarCliente(JTextField id, JTextField nombres, JTextField apellido, JTextField dni, JTextField telefono, JTextField email) {
        
        String sql = "UPDATE cliente SET cliente.nombres=?, cliente.apellido=?, cliente.dni=?, cliente.telefono=?, cliente.email=? WHERE cliente.idCliente=?";

        try {
            cliente.setIdCliente(Integer.parseInt(id.getText()));
            cliente.setNombre(nombres.getText());
            cliente.setApellido(apellido.getText());
            cliente.setDni(dni.getText());
            cliente.setTelefono(telefono.getText());
            cliente.setEmail(email.getText());

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea modificar los datos del cliente?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta
                cs.setString(1, cliente.getNombre());
                cs.setString(2, cliente.getApellido());
                cs.setString(3, cliente.getDni());
                cs.setString(4, cliente.getTelefono());
                cs.setString(5, cliente.getEmail());
                cs.setInt(6, cliente.getIdCliente());

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "El cliente fue modificado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La modificación fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar cliente: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
    
    public void eliminarCliente(JTextField id){
       
        String sql = "DELETE FROM cliente WHERE cliente.idCliente = ?";
        
        try {
            cliente.setIdCliente(Integer.parseInt(id.getText()));

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea Eliminar este cliente?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta              
                cs.setInt(1, cliente.getIdCliente());

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "El cliente fue eliminado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La eliminacion fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
}
