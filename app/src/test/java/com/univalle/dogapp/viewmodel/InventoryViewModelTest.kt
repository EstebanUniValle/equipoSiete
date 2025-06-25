package com.univalle.dogapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.univalle.dogapp.repository.InventoryRepository
import com.univalle.dogapp.model.Inventory
import com.univalle.dogapp.model.BreedsResponse
import com.univalle.dogapp.model.BreedImageResponse
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.junit.Before
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import kotlinx.coroutines.test.resetMain
import org.junit.After
import kotlinx.coroutines.delay
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.doAnswer
import org.junit.Assert.assertNull
import org.mockito.Mockito.doReturn


class InventoryViewModelTest {

 @get:Rule
 val rule = InstantTaskExecutorRule()

 lateinit var inventoryViewModel: InventoryViewModel

 @Mock
 lateinit var inventoryRepository: InventoryRepository

 @Before
 fun setUp() {
  MockitoAnnotations.openMocks(this)
  inventoryViewModel = InventoryViewModel(inventoryRepository)
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `test método saveInventory`() = runBlocking {
      // given
      Dispatchers.setMain(UnconfinedTestDispatcher())
      val inventory = Inventory(
       id = 1,
       name = "Luna",
       breed = "Golden Retriever",
       owner = "Juan Pérez",
       phone = "3204567890",
       symptom = "Fractura extremidad",
       imagen = "https://images.dog.ceo/breeds/retriever-golden/n02099601_123.jpg"
      )
      // when
      inventoryViewModel.saveInventory(inventory)
      // then
      verify(inventoryRepository).saveInventory(inventory)
 }

 @Test
 fun `test método getListInventory`() = runBlocking {
     // given
      Dispatchers.setMain(UnconfinedTestDispatcher())
      val mockList = mutableListOf(
       Inventory(
        id = 1,
        name = "Luna",
        breed = "Golden Retriever",
        owner = "Juan Pérez",
        phone = "3204567890",
        symptom = "Fractura extremidad",
        imagen = "https://images.dog.ceo/breeds/retriever-golden/n02099601_123.jpg"
       )
      )
       // when
      `when`(inventoryRepository.getListInventory()).thenReturn(mockList)
      inventoryViewModel.getListInventory()
       // then
      assertEquals(mockList, inventoryViewModel.listInventory.value)
 }

 @Test
 fun `test metodo deleteInventory actualiza _deleted a true`() = runBlocking {
        // given
       Dispatchers.setMain(UnconfinedTestDispatcher())
       val mockInventory = Inventory(
        id = 1,
        name = "Luna",
        breed = "Golden Retriever",
        owner = "Juan Pérez",
        phone = "3204567890",
        symptom = "Fractura extremidad",
        imagen = "https://images.dog.ceo/breeds/retriever-golden/n02099601_123.jpg"
       )
       // when
       doAnswer {
       }.`when`(inventoryRepository).deleteInventory(mockInventory)
       inventoryViewModel.deleteInventory(mockInventory)

       delay(100)
       // then
       assertEquals(true, inventoryViewModel.deleted.value)
 }

 @Test
 fun `test metodo deleteInventory retorna false cuando hay error`() = runBlocking {
       // given
      Dispatchers.setMain(UnconfinedTestDispatcher())
      val mockInventory = Inventory(
       id = 1,
       name = "Luna",
       breed = "Golden Retriever",
       owner = "Juan Pérez",
       phone = "3204567890",
       symptom = "Fractura extremidad",
       imagen = "https://images.dog.ceo/breeds/retriever-golden/n02099601_123.jpg"
      )
      // when
      doThrow(RuntimeException("Error al eliminar"))
       .`when`(inventoryRepository).deleteInventory(mockInventory)
      inventoryViewModel.deleteInventory(mockInventory)

      delay(100)
     // then
      assertNotEquals(true, inventoryViewModel.deleted.value)
 }

 @Test
 fun `getBreeds actualiza breedsList con razas del repositorio`() = runBlocking {
      // given
      val fakeMap = mapOf("labrador" to listOf<String>())
      val fakeResponse = BreedsResponse(message = fakeMap, status = "success")

      // cuando se llame al repo, devolvemos fakeResponse
      `when`(inventoryRepository.getBreeds()).thenReturn(fakeResponse)
      inventoryViewModel.getBreeds()
      delay(100)

      // Then
      assertEquals(listOf("labrador"), inventoryViewModel.breedsList.value)
 }


 @Test
 fun `test getBreedImage retorna URL de imagen si status es success`() = runBlocking {
      // given
      val breed = "labrador"
      val expectedUrl = "https://images.dog.ceo/breeds/labrador/image123.jpg"
      val response = BreedImageResponse(expectedUrl, "success")
      // when
      doReturn(response).`when`(inventoryRepository).getBreedImage(breed)
      val result = inventoryViewModel.getBreedImage(breed)
     // Then
      assertEquals(expectedUrl, result)
 }

 @Test
 fun `test getBreedImage retorna null si ocurre excepcion`() = runBlocking {
        // given
        val breed = "labrador"
        doThrow(RuntimeException("Network error"))
        // when
        .`when`(inventoryRepository).getBreedImage(breed)

        val result = inventoryViewModel.getBreedImage(breed)
        // Then
        assertNull(result)
 }

}