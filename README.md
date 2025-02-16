# Sistema-POS-escritorio-jdbc
Sistema de ventas con Java, JDBC, MySQL, Swing
Proyecto POS - Sistema de Punto de Venta
Este proyecto es una aplicación de Punto de Venta (POS) desarrollada en Java, utilizando JDBC para la gestión de la base de datos y Swing para la interfaz gráfica de usuario. El sistema permite a los usuarios gestionar productos, realizar ventas y registrar transacciones, todo con una interfaz sencilla y fácil de usar.

Tecnologías utilizadas:
Java: Lenguaje de programación principal para la lógica de negocio y la interfaz de usuario.
JDBC: Conexión a la base de datos y operaciones CRUD (crear, leer, actualizar, eliminar) en las tablas de productos, ventas y transacciones.
MySQL/SQLite: Base de datos para almacenar productos, ventas, inventarios y transacciones.
Swing: Framework para la creación de la interfaz gráfica, diseñada para facilitar la interacción del usuario con el sistema.
Patrón MVC (Modelo-Vista-Controlador): Implementación de este patrón arquitectural para separar claramente las responsabilidades de la lógica de negocio (Modelo), la interfaz de usuario (Vista) y la gestión de eventos (Controlador).
Características:
Gestión de Productos: Permite agregar, eliminar y modificar productos en el inventario.
Registro de Ventas: Facilita el registro de ventas, calculando el total de la transacción y actualizando el inventario automáticamente.
Manejo de Pagos: Se registran los pagos realizados, vinculándolos con las ventas correspondientes.
Reportes: Generación de reportes para el seguimiento de ventas, inventarios y transacciones realizadas.
Interfaz Intuitiva: La interfaz de usuario es amigable, utilizando Swing para crear formularios de entrada, botones de acción y visualización de datos en tiempo real.
Objetivo:
El propósito de este proyecto es proporcionar una solución eficiente para la gestión de ventas, productos y transacciones en un sistema de punto de venta, sin depender de frameworks de persistencia como JPA, y manteniendo un control más directo sobre las consultas SQL y la gestión de la base de datos.
