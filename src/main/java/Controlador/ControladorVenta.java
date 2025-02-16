package Controlador;

import Configuracion.Conexion;
import Modelo.Cliente;
import Modelo.Producto;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Emilio Mayer
 */
public class ControladorVenta {
    
    Conexion conexion = new Conexion(); 
    Producto producto = new Producto();
    Cliente cliente = new Cliente();
    
    
     //Busqueda tiempo real de clientes
     public void buscarCliente(JTextField nombreCliente, JTable tablaClientes){
        
        //Creamos el modelo de tabla
        DefaultTableModel modelo = new DefaultTableModel(){
            //Inhabilita la edicion de la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //Añade los titulos al modelo de la tabla
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("DNI");
        
        try {
            String sql = "SELECT * FROM cliente WHERE cliente.nombres like concat('%',?,'%')";
            
            PreparedStatement ps = conexion.establecerConexion().prepareStatement(sql);
            ps.setString(1, nombreCliente.getText());
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {                
                cliente.setIdCliente(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("nombres"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getString("dni"));
                
                modelo.addRow(new Object[]{cliente.getIdCliente(), cliente.getNombre(), cliente.getApellido(), cliente.getDni()});
                
            }
            tablaClientes.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar el cliente " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
        
    }
    
    //Busqueda tiempo real de productos
    public void buscarProducto(JTextField nombreProducto, JTable tablaProductos){
        
        //Creamos el modelo de tabla
        DefaultTableModel modelo = new DefaultTableModel(){
            //Inhabilita la edicion de la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //Añade los titulos al modelo de la tabla
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        
        try {
            String sql = "SELECT * FROM producto WHERE producto.nombre like concat('%',?,'%')";
            
            PreparedStatement ps = conexion.establecerConexion().prepareStatement(sql);
            ps.setString(1, nombreProducto.getText());
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {                
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioProducto(rs.getDouble("precioProducto"));
                producto.setStock(rs.getInt("stock"));
                
                modelo.addRow(new Object[]{producto.getIdProducto(), producto.getNombre(), producto.getPrecioProducto(), producto.getStock()});
                
            }
            tablaProductos.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar el producto " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
        
    }
    
    public void crearComprobante(JTextField codCliente){
        
        String sql = "INSERT INTO comprobante (fechaComprobante, fkCliente) VALUES (curdate(),?);";
        
        try {
            cliente.setIdCliente(Integer.parseInt(codCliente.getText()));
            
            CallableStatement cs = conexion.establecerConexion().prepareCall(sql);
            cs.setInt(1, cliente.getIdCliente());
            
            cs.execute();
            JOptionPane.showMessageDialog(null, "Comprobante creado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear comprobante"+ e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
    
    public void crearVenta(JTable tablaResumenVenta){
        
        String consultaDetalle = "INSERT INTO detalle (fkComprobante, fkProducto, cantidad, precioVenta) VALUES ((SELECT MAX(idComprobante) FROM comprobante), ?, ?, ?);";
        String consultaStock = "UPDATE producto SET producto.stock = stock - ? WHERE idProducto = ?";
        
        try {
            PreparedStatement psDetalle = conexion.establecerConexion().prepareStatement(consultaDetalle);
            PreparedStatement psStock = conexion.establecerConexion().prepareStatement(consultaStock);
            
            int filas = tablaResumenVenta.getRowCount();
            
            for (int i = 0; i < filas; i++) {
                int idProducto = Integer.parseInt(tablaResumenVenta.getValueAt(i, 0).toString());
                int cantidad = Integer.parseInt(tablaResumenVenta.getValueAt(i, 3).toString());
                
                double precioVenta = Double.parseDouble(tablaResumenVenta.getValueAt(i, 2).toString());
                

                
                psDetalle.setInt(1, idProducto);
                psDetalle.setInt(2, cantidad);
                psDetalle.setDouble(3, precioVenta);
                psDetalle.execute();
                
                psStock.setInt(1, cantidad);
                psStock.setInt(2, idProducto);
                psStock.execute();
                
            }
            
            JOptionPane.showMessageDialog(null, "Venta Confirmada");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al confirmar venta"+e.getMessage());
        } finally {
            conexion.cerrarConexion();
        }
    }
    
    public void mostrarUltimoComprobante(JLabel ultimoComprobante){
        try {
            String sql = "SELECT MAX(idComprobante) as ultimoComprobante FROM comprobante";
            
            PreparedStatement ps = conexion.establecerConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ultimoComprobante.setText(String.valueOf(rs.getInt("ultimoComprobante")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error al cargar ultimo comprobante: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }
}
