<template>
  <section class="layout">
    <div class="header">
      <div class="header-content" align="center">
        <h1>Librería García</h1>
        <h3>-Lee de noche, lee de día-</h3>
      </div>
    </div>

    <div class="body" align="center">
      <h2>Inventario</h2>

      <!-- Búsqueda -->
      <div class="search-container">
        <input
          v-model="searchTerm"
          type="text"
          placeholder="Buscar por título o autor"
          @input="searchBooks"
        />
      </div>

      <v-table height="700px" fixed-header>
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
          <tr v-for="item in filteredLibros" :key="item.id">
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

    <div class="footer">
      <AppFooter />
    </div>
  </section>
</template>

<script>
// Usamos axios para la conexión
import axios from 'axios';

export default {
  name: 'Inventario',
  data() {
    return {
      libros: [],
      searchTerm: '',
    };
  },
  computed: {
    // Filtra los libros según el término de búsqueda
    filteredLibros() {
      return this.libros.filter((libro) => {
        const term = this.searchTerm.toLowerCase();
        return libro.titulo.toLowerCase().includes(term) || libro.autor.toLowerCase().includes(term);
      });
    }
  },
  mounted() {
    // Verificamos que los libros solo se asignen una vez
    if (this.libros.length === 0) {
      axios
        .get('http://localhost:8081/libreria/')
        .then((response) => {
          // Asignamos los datos solo si no están ya en la variable
          this.libros = response.data;
        })
        .catch((error) => {
          console.error(error);
        });
    }
  },
  methods: {
    // Filtra los libros cada vez que el usuario escribe en el campo de búsqueda
    searchBooks() {
      if (this.searchTerm.trim() === '') {
        // Si no hay término de búsqueda, obtenemos todos los libros
        axios
          .get('http://localhost:8081/libreria/')
          .then((response) => {
            this.libros = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
      } else {
        // Si hay un término de búsqueda, filtramos los libros
        axios
          .get(`http://localhost:8081/libreria/busqueda/?search=${this.searchTerm}`)
          .then((response) => {
            this.libros = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }
  }
};
</script>

<style scoped>
.layout {
  display: grid;
  grid-template-rows: auto 1fr auto;
  grid-template-columns: auto 1fr auto;
  gap: 8px;
  width: 100%;
}

.header {
  grid-column: 1 / -1;
  background-color: #21946e6e;
  color: #fff;
  padding: 16px;
}

.body {
  grid-column: 1 / -1;
  align-self: center;
}

.footer {
  grid-column: 1 / -1;
  padding: 16px;
  background-color: #f1f1f1;
}

.header-content {
  text-align: center;
}

h3 {
  color: #969696;
  font-size: 18px;
  font-weight: 400;
}

.search-container {
  margin-bottom: 16px;
}

input[type="text"] {
  padding: 8px;
  font-size: 16px;
  width: 300px;
  margin-bottom: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.v-table {
  align-self: center;
  max-width: 90%;
  border-radius: 9px;
}

.color-cabeza-tabla {
  background-color: #34f3b46e;
  color: #fff;
}

th, td {
  padding: 8px;
}

th {
  text-align: left;
}
</style>
