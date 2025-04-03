<template>
  <section class="layout">
    <div class="header">
      <div align="center">
        <h1>Librería García</h1>
        <h3>Lee de noche, lee de día</h3>
      </div>
    </div>

    <div class="body" align="center">
      <h2>Inventario</h2>
      <v-table height="700px" fixed-header>
        <!--
        "id": 3,
        "titulo": "1984",
        "autor": "George Orwell",
        "precio": 18000,
        "stock": 12,
        "anio": 1949,
        "descripcion": "Distopía sobre el control gubernamental"
        -->
        <thead class="color-cabeza-tabla">
          <tr>
            <th class="text-left">Título</th>
            <th class="text-left">Autor</th>
            <th class="text-left">Descripción</th>
            <th class="text-left">Año</th>
            <th class="text-left">Stock</th>
            <th class="text-left">Precio</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="item in libros"
            :key="item.id"
          >
            <td>{{ item.titulo }}</td>
            <td>{{ item.autor }}</td>
            <td>{{ item.descripcion }}</td>
            <td>{{ item.anio }}</td>
            <td>{{ item.stock }}</td>
            <td>{{ item.precio }}</td>
          </tr>
        </tbody>
      </v-table>
    </div>

    <div class="rightSide">
      <br>
    </div>

    <div class="footer">
      <AppFooter />
    </div>
  </section>
</template>

<script>
  //usaremos axios para la conexión
  import axios from 'axios';

  export default {
    name: 'Inventario',
    data: () => ({
      libros: []
    }),
    mounted() {
      axios
        .get('http://localhost:8080/libreria/')
        .then(response => {
          this.libros = response.data;
        })
        .catch(error => {
          console.log(error);
        });
    }}
</script>

<style scoped>
  .layout {
      width: 100%;

      display: grid;
      grid:
        "header header header" auto
        "leftSide body rightSide" 1fr
        "footer footer footer" auto
        / auto 1fr auto;
      gap: 8px;
    }

  .header { grid-area: header; }
  .leftSide { grid-area: leftSide; }
  .body { grid-area: body; }
  .rightSide { grid-area: rightSide; }
  .footer { grid-area: footer; }

  .header {
    background-color: #21946e6e;
    color: #fff;
    padding: 16px;
  }

  .body {
    align-self: center;
  }
  .v-table {
    align-self: center;
    max-width: 90%;
    border-radius: 9px;
  }

  h3{
    color: #969696;
    font-size: 18px;
    font-weight: 400;
  }

  .color-cabeza-tabla{
    background-color: #34f3b46e;
    color: #fff;
  }
</style>
