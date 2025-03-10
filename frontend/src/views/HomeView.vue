<template>
  <div class="content">
    <div class="banner-new-items">
      <div class="banner-title">
        <h2>НОВИНКИ</h2>
        <h4>Посмотреть все</h4>
      </div>
      <div class="banner-img">
        <img src="@/assets/img/banner.jpg" alt="" />
      </div>
    </div>
    <div class="categories">
      <div class="category-title">
        <h2>ТАКЖЕ ПОСМОТРИТЕ</h2>
      </div>
      <div class="categories">
        <ul class="category-items">
          <li>
            <img src="@/assets/img/category-item1.png" alt="" />
            <h3>Верхняя одежда</h3>
          </li>
          <li>
            <img src="@/assets/img/category-item2.png" alt="" />
            <h3>Обувь</h3>
          </li>
          <li>
            <img src="@/assets/img/category-item3.png" alt="" />
            <h3>Футболки</h3>
          </li>
          <li>
            <img src="@/assets/img/category-item4.png" alt="" />
            <h3>Аксессуары</h3>
          </li>
          <li>
            <img src="@/assets/img/category-item5.png" alt="" />
            <h3>Джинсы</h3>
          </li>
        </ul>
      </div>
    </div>
    <div class="collections">
        <div class="collections-items">
      <div class="collections-title">
        <h2>Коллекция - 1</h2>
        <h4>Посмотреть все</h4>
      </div>
      <div class="collections-img">
        <img src="@/assets/img/banner.jpg" alt="" />
      </div>
    </div>
    <div class="collections-items">
      <div class="collections-title">
        <h2>Коллекция - 2</h2>
        <h4>Посмотреть все</h4>
      </div>
      <div class="collections-img">
        <img src="@/assets/img/banner.jpg" alt="" />
      </div>
    </div>


    <div class="promo"></div>
  </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
/* .content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
} */

.banner-new-items {
  position: relative;
  text-align: center;
}

.banner-title {
  position: absolute;
  bottom: 250px; /* Отступ снизу */
  left: 50%;
  transform: translateX(-50%);
  color: white;
  font-size: 2rem;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 5px;
  text-align: center;
}

.banner-title h4 {
  font-size: 1rem;
  margin-top: 5px;
  text-decoration: underline;
  cursor: pointer;
}

.banner-img img {
  max-width: 100%; /* Чтобы изображение не выходило за границы контейнера */
  height: auto; /* Автоматическая высота в зависимости от пропорций */
  display: block;
  object-fit: cover; /* Обрезка изображения без искажения */
}


.category-title {
  font-family: 'Martian Mono', monospace;
  text-align: center;
}

.category-items {
  display: flex;
  gap: 15px;
  list-style: none;
  padding: 0;
  justify-content: center;
}

.category-items li {
  flex: 1;
  max-width: 300px;
}

.category-items li h3{
    text-align: center;
    font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}

.category-items img {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 10px;
}

.collections {
  height: 80vh; /* 80% экрана */
  overflow-y: auto;
  scroll-snap-type: y mandatory; /* Прокрутка по секциям */
}

.collections-items {
  height: 80vh; /* Каждая секция занимает 80% экрана */
  position: relative;
  scroll-snap-align: start; /* Привязка к началу секции */
}

.collections-img {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 80vh;
  z-index: -1; /* Фиксированное фоновое изображение */
}

.collections-img img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* Заполнение без искажений */
}

.collections-title {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  text-align: center;
}

</style>
