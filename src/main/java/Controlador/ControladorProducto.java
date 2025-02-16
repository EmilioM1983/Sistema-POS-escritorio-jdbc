package Controlador;

import Configuracion.Conexion;
import Modelo.Producto;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Emilio Mayer
 */
public class ControladorProducto {
    Conexion conexion = new Conexion();
    Producto producto = new Producto();

    //Trae todos los clientes
    public void mostrarProductos(JTable tablaTotalProductos) {
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
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");

        //Asignamos el modelo a la tabla que recibe la funcion por parametro
        tablaTotalProductos.setModel(modelo);

        //Sentencia sql
        sql = "SELECT producto.idProducto, producto.nombre, producto.descripcion, producto.precioProducto, producto.stock FROM producto";

        try {
            //Establece conexion con bd y ejecuta la consulta sql
            Statement st = conexion.establecerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                //Obtiene los datos de la bd para añadirlos posteriormente al modelo
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecioProducto(rs.getDouble("precioProducto"));
                producto.setStock(rs.getInt("Stock"));

                //Añade un registro la modelo
                modelo.addRow(new Object[]{producto.getIdProducto(), producto.getNombre(), producto.getDescripcion(),
                    producto.getPrecioProducto(), producto.getStock()});
            }

            //Se le asigna el modelo con los titulos y contenido de la bd a la tabla
            tablaTotalProductos.setModel(modelo);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al mostrar productos: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
    
    //Agregar Producto
    public void AgregarProducto(JTextField nombre, JTextArea descripcion, JTextField precioProducto, JTextField stock) {

        

        String sql = "INSERT INTO producto (nombre, descripcion, precioProducto, stock) VALUES (?,?,?,?)";

        try {
            producto.setNombre(nombre.getText());
            producto.setDescripcion(descripcion.getText());
            producto.setPrecioProducto(Double.parseDouble( precioProducto.getText()));
            producto.setStock(Integer.parseInt(stock.getText()));

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de registrar los datos del producto?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);
                cs.setString(1, producto.getNombre());
                cs.setString(2, producto.getDescripcion());
                cs.setString(3, String.valueOf( producto.getPrecioProducto()));
                cs.setString(4, String.valueOf(producto.getStock()));

                cs.execute();

                JOptionPane.showMessageDialog(null, "El producto fue registrado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Se cancelo guardar producto");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + ex.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
    
    public void modificarProducto(JTextField id, JTextField nombre, JTextArea descripcion, JTextField precioProducto, JTextField stock) {
        
        String sql = "UPDATE producto SET producto.nombre=?, producto.descripcion=?, producto.precioProducto=?, producto.stock=? WHERE producto.idProducto=?";

        try {
            producto.setIdProducto(Integer.parseInt(id.getText()));
            producto.setNombre(nombre.getText());
            producto.setDescripcion(descripcion.getText());
            producto.setPrecioProducto(Double.parseDouble(precioProducto.getText()));
            producto.setStock(Integer.parseInt(stock.getText()));

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea modificar los datos del producto?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta
                cs.setString(1, producto.getNombre());
                cs.setString(2, producto.getDescripcion());
                cs.setDouble(3, producto.getPrecioProducto());
                cs.setInt(4, producto.getStock());
                cs.setInt(5, producto.getIdProducto());
                

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "El producto fue modificado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La modificación fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar cliente: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
    
    public void eliminarProducto(JTextField id){
       
        String sql = "DELETE FROM producto WHERE producto.idProducto = ?";
        
        try {
            producto.setIdProducto(Integer.parseInt(id.getText()));

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea Eliminar este producto?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta              
                cs.setInt(1, producto.getIdProducto());

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "El producto fue eliminado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La eliminacion fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
}
