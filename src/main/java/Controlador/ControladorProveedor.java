package Controlador;

import Configuracion.Conexion;
import Modelo.Proveedor;
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
public class ControladorProveedor {

    Conexion conexion = new Conexion();
    Proveedor proveedor = new Proveedor();

    //Trae todos los clientes
    public void mostrarProveedores(JTable tablaTotalProveedores) {

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
        modelo.addColumn("Razon Social");
        modelo.addColumn("Cuit");
        modelo.addColumn("Telefono");
        modelo.addColumn("email");

        //Asignamos el modelo a la tabla que recibe la funcion por parametro
        tablaTotalProveedores.setModel(modelo);

        //Sentencia sql
        sql = "SELECT proveedor.idProveedor, proveedor.razonSocial, proveedor.cuit, proveedor.telefono, proveedor.email FROM proveedor";

        try {
            //Establece conexion con bd y ejecuta la consulta sql
            Statement st = conexion.establecerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                //Obtiene los datos de la bd para añadirlos posteriormente al modelo
                proveedor.setIdProveedor(rs.getInt("idProveedor"));
                proveedor.setRazonSocial(rs.getString("razonSocial"));
                proveedor.setCuit(rs.getString("cuit"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));

                //Añade un registro la modelo
                modelo.addRow(new Object[]{proveedor.getIdProveedor(), proveedor.getRazonSocial(),
                    proveedor.getCuit(), proveedor.getTelefono(), proveedor.getEmail()});
            }

            //Se le asigna el modelo con los titulos y contenido de la bd a la tabla
            tablaTotalProveedores.setModel(modelo);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al mostrar proveedores: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    //Agregar Cliente
    public void AgregarProveedor(JTextField razonSocial,JTextField cuit, JTextField telefono, JTextField email) {

        String sql = "INSERT INTO proveedor (razonSocial, cuit, telefono, email) VALUES (?,?,?,?)";

        try {
            proveedor.setRazonSocial(razonSocial.getText());
            proveedor.setCuit(cuit.getText());
            proveedor.setTelefono(telefono.getText());
            proveedor.setEmail(email.getText());

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de registrar los datos del proveedor?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);
                cs.setString(1, proveedor.getRazonSocial());
                cs.setString(2, proveedor.getCuit());
                cs.setString(3, proveedor.getTelefono());
                cs.setString(4, proveedor.getEmail());

                cs.execute();

                JOptionPane.showMessageDialog(null, "El nuevo proveedor fue registrado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Se cancelo guardar proveedor");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar nuevo proveedor: " + ex.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    public void modificarProveedor(JTextField id, JTextField razonSocial, JTextField cuit, JTextField telefono, JTextField email) {
        
        String sql = "UPDATE proveedor SET proveedor.razonSocial=?, proveedor.cuit=?, proveedor.telefono=?, proveedor.email=? WHERE proveedor.idProveedor=?";

        try {
            proveedor.setIdProveedor(Integer.parseInt(id.getText()));
            proveedor.setRazonSocial(razonSocial.getText());
            proveedor.setCuit(cuit.getText());
            proveedor.setTelefono(telefono.getText());
            proveedor.setEmail(email.getText());

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea modificar los datos del proveedor?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta
                cs.setString(1, proveedor.getRazonSocial());
                cs.setString(2, proveedor.getCuit());
                cs.setString(3, proveedor.getTelefono());
                cs.setString(4, proveedor.getEmail());
                cs.setInt(5, proveedor.getIdProveedor());

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "El proveedor fue modificado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La modificación fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar proveedor: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
    
    public void eliminarCliente(JTextField id){
       
        String sql = "DELETE FROM proveedor WHERE proveedor.idProveedor = ?";
        
        try {
            proveedor.setIdProveedor(Integer.parseInt(id.getText()));

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea Eliminar este proveedor?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta              
                cs.setInt(1, proveedor.getIdProveedor());

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "El proveedor fue eliminado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La eliminacion fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
}
