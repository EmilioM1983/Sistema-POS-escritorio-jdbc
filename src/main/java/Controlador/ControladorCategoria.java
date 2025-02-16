package Controlador;

import Configuracion.Conexion;
import Modelo.Categoria;
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
public class ControladorCategoria {

    Conexion conexion = new Conexion();
    Categoria categoria = new Categoria();

    //Trae todos los clientes
    public void mostrarCategorias(JTable tablaTotalCategorias) {

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

        //Asignamos el modelo a la tabla que recibe la funcion por parametro
        tablaTotalCategorias.setModel(modelo);

        //Sentencia sql
        sql = "SELECT categoria_producto.idCategoriaProducto, categoria_producto.nombre FROM categoria_producto";

        try {
            //Establece conexion con bd y ejecuta la consulta sql
            Statement st = conexion.establecerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                //Obtiene los datos de la bd para añadirlos posteriormente al modelo
                categoria.setIdCategoria(rs.getInt("idCategoriaProducto"));
                categoria.setNombre(rs.getString("nombre"));

                //Añade un registro la modelo
                modelo.addRow(new Object[]{categoria.getIdCategoria(), categoria.getNombre()});
            }

            //Se le asigna el modelo con los titulos y contenido de la bd a la tabla
            tablaTotalCategorias.setModel(modelo);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al mostrar categorias: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    //Agregar Cliente
    public void AgregarCategoria(JTextField nombres) {

        String sql = "INSERT INTO categoria_producto (nombre) VALUES (?)";

        try {
            categoria.setNombre(nombres.getText());

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de registrar la categoria?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);
                cs.setString(1, categoria.getNombre());

                cs.execute();

                JOptionPane.showMessageDialog(null, "La nueva categoria fue registrado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Se cancelo guardar categoria");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar nueva categoria: " + ex.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    public void modificarCategoria(JTextField id, JTextField nombres) {

        String sql = "UPDATE categoria_producto SET categoria_producto.nombre=? WHERE categoria_producto.idCategoriaProducto=?";

        try {
            categoria.setIdCategoria(Integer.parseInt(id.getText()));
            categoria.setNombre(nombres.getText());

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea modificar la categoria?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta
                cs.setString(1, categoria.getNombre());
                cs.setInt(2, categoria.getIdCategoria());

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "La categoria fue modificada exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La modificación fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar categoria: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    public void eliminarCategoria(JTextField id) {

        String sql = "DELETE FROM categoria_producto WHERE categoria_producto.idCategoriaProducto = ?";

        try {
            categoria.setIdCategoria(Integer.parseInt(id.getText()));

            // Mensaje de confirmación
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de que desea Eliminar esta categoria?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //Preparacion de la consulta
                CallableStatement cs = conexion.establecerConexion().prepareCall(sql);

                //Asignación de parámetros en la consulta              
                cs.setInt(1, categoria.getIdCategoria());

                //Ejecución de la consulta
                cs.execute();

                JOptionPane.showMessageDialog(null, "La categoria fue eliminado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "La eliminacion fue cancelada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar categoria: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
}
