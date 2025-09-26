package com.tzh.mamp.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.data.model.QuizQuestion
import com.tzh.mamp.data.model.VowelLetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.immutableListOf
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyanmarLetterRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Independent vowels with dependent equivalents
    val vowelLetters = listOf(
        VowelLetter(id = 1, symbol = "အ - အာ - အား", sound = "/ʔa/", example = "အမ (mother)", equivalent = "အ", filePath = "vowels/ah.mp3"),
        VowelLetter(id = 2, symbol = "အိ - အီ - အီး", sound = "/ʔi/", example = "ဤ (this)", equivalent = "အိ", filePath = "vowels/e.mp3"),
        VowelLetter(id = 3, symbol = "ဥ - အူ - ဦး", sound = "/ʔu/", example = "ဥ (egg)", equivalent = "ဥ", filePath = "vowels/u.mp3"),
        VowelLetter(id = 4, symbol = "အေ့ - အေ - အေး", sound = "/ʔe/", example = "အေး (cool)", equivalent = "အေ", filePath = "vowels/ay.mp3"),
        VowelLetter(id = 5, symbol = "ဧည့် - အယ် - အဲ", sound = "/ʔɛ/", example = "ဧရာဝတီ (Ayeyarwady River)", equivalent = "အဲ", filePath = "vowels/eare.mp3"),
        VowelLetter(id = 6, symbol = "အော့ - အော် - ဩ", sound = "/ʔɔ/", example = "ဩဇာ (influence)", equivalent = "အော", filePath = "vowels/au.mp3"),
        VowelLetter(id = 7, symbol = "အံ့ - အန် - အမ်း", sound = "/ʔan/", example = "အံ့ (surprise)", equivalent = "အန်", filePath = "vowels/an.mp3"),
        VowelLetter(id = 8, symbol = "အို့ - အို - အိုး", sound = "/ʔo/", example = "အိုး (pot)", equivalent = "အို", filePath = "vowels/o.mp3"),
        VowelLetter(id = 9, symbol = "အင့် - အင် - အင်း", sound = "/ʔin/", example = "အင်း (yes)", equivalent = "အင်", filePath = "vowels/in.mp3"),
        VowelLetter(id = 10, symbol = "အောင့် - အောင် - အောင်း", sound = "/ʔaʊɴ/", example = "အောင် (success)", equivalent = "အောင်", filePath = "vowels/oun.mp3"),
        VowelLetter(id = 11, symbol = "အိုင့် - အိုင် - အိုင်း", sound = "/ʔaiɴ/", example = "အိုင် (pond)", equivalent = "အိုင်", filePath = "vowels/ine.mp3"),
        VowelLetter(id = 12, symbol = "အိမ့် - အိမ် - အိမ်း", sound = "/ʔeiɴ/", example = "အိမ် (house)", equivalent = "အိမ်", filePath = "vowels/ain.mp3"),
        VowelLetter(id = 13, symbol = "အုန့် - အုန် - အုန်း", sound = "/ʔouɴ/", example = "အုန်း (coconut)", equivalent = "အုန်း", filePath = "vowels/ome.mp3"),
        VowelLetter(id = 14, symbol = "အွန့် - အွန် - အွန်း", sound = "/ʔuɴ/", example = "အွန်း (warm)", equivalent = "အွန်း", filePath = "vowels/oon.mp3")
    )


    val consonants = immutableListOf(
        Consonant(1, "က", "/ka/", "ကလေး (child)", "Picture of a smiling child", "consonant/က.mp3"),
        Consonant(2, "ခ", "/kʰa/", "ခရမ်းချဉ် (eggplant)", "Purple eggplant", "consonant/ခ.mp3"),
        Consonant(3, "ဂ", "/ga/", "ဂီတ (music)", "Musical notes or guitar", "consonant/ဂ.mp3"),
        Consonant(4, "ဃ", "/gʰa/", "ဃနာ (rare word)", "Optional or skip for simplicity", "consonant/ဃ.mp3"),
        Consonant(5, "င", "/ŋa/", "ငါး (fish)", "Cartoon fish swimming", "consonant/င.mp3"),
        Consonant(6, "စ", "/sa/", "စက် (machine)", "Toy robot or gears", "consonant/စ.mp3"),
        Consonant(7, "ဆ", "/sʰa/", "ဆရာ (teacher)", "Teacher with chalkboard", "consonant/ဆ.mp3"),
        Consonant(8, "ဇ", "/za/", "ဇာတ်ကား (movie)", "Film reel or cinema screen", "consonant/ဇ.mp3"),
        Consonant(9, "ဈ", "/zʰa/", "ဈေး (market)", "Market stall with fruits", "consonant/ဈ.mp3"),
        Consonant(10, "ည", "/ɲa/", "ညစာ (dinner)", "Plate of food", "consonant/ည.mp3"),
        Consonant(11, "ဋ", "/ṭa/", "ဋီ (rare)", "Optional or skip", "consonant/ဋ.mp3"),
        Consonant(12, "ဌ", "/ṭʰa/", "ဌာန (department)", "Office building icon", "consonant/ဌ.mp3"),
        Consonant(13, "ဍ", "/ḍa/", "ဍာ (rare)", "Optional or skip", "consonant/ဍ.mp3"),
        Consonant(14, "ဎ", "/ḍʰa/", "ဎန (rare)", "Optional or skip", "consonant/ဎ.mp3"),
        Consonant(15, "ဏ", "/ṇa/", "ဏာ (rare)", "Optional or skip", "consonant/ဏ.mp3"),
        Consonant(16, "တ", "/ta/", "တံတား (bridge)", "Cartoon bridge over river", "consonant/တ.mp3"),
        Consonant(17, "ထ", "/tʰa/", "ထမင်း (rice)", "Bowl of rice", "consonant/ထ.mp3"),
        Consonant(18, "ဒ", "/da/", "ဒေါက်တာ (doctor)", "Doctor with stethoscope", "consonant/ဒ.mp3"),
        Consonant(19, "ဓ", "/dʰa/", "ဓာတ်ပုံ (photo)", "Camera or photo frame", "consonant/ဓ.mp3"),
        Consonant(20, "န", "/na/", "နေ (sun)", "Bright sun icon", "consonant/န.mp3"),
        Consonant(21, "ပ", "/pa/", "ပန်း (flower)", "Colorful flower", "consonant/ပ.mp3"),
        Consonant(22, "ဖ", "/pʰa/", "ဖရဲသီး (watermelon)", "Watermelon slice", "consonant/ဖ.mp3"),
        Consonant(23, "ဗ", "/ba/", "ဗီဒီယို (video)", "Play button or screen", "consonant/ဗ.mp3"),
        Consonant(24, "ဘ", "/bʰa/", "ဘုရား (temple)", "Pagoda or temple icon", "consonant/ဘ.mp3"),
        Consonant(25, "မ", "/ma/", "မိခင် (mother)", "Mother and child", "consonant/မ.mp3"),
        Consonant(26, "ယ", "/ya/", "ယာဉ် (vehicle)", "Car or bus", "consonant/ယ.mp3"),
        Consonant(27, "ရ", "/ra/", "ရေ (water)", "Splash or water droplet", "consonant/ရ.mp3"),
        Consonant(28, "လ", "/la/", "လ (moon)", "Crescent moon", "consonant/လ.mp3"),
        Consonant(29, "ဝ", "/wa/", "ဝက် (pig)", "Cartoon pig", "consonant/ဝ.mp3"),
        Consonant(30, "သ", "/θa/", "သစ်ပင် (tree)", "Tree with leaves", "consonant/သ.mp3"),
        Consonant(31, "ဟ", "/ha/", "ဟင်း (curry)", "Bowl of curry", "consonant/ဟ.mp3"),
        Consonant(32, "ဠ", "/ḷa/", "ဠာ (rare)", "Optional or skip", "consonant/ဠ.mp3"),
        Consonant(33, "အ", "/ʔa/", "အိမ် (house)", "House icon", "consonant/အ.mp3")
    )


    private val alphabetsCollection = firestore.collection("Alphabets")
    private val vowelsCollection = firestore.collection("Vowels")


    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            getData()
//            setList()
//        }
    }

    suspend fun deleteAllAlphabet() {
        firestore.deleteAllDocumentsInCollection(alphabetsCollection)
    }

    suspend fun setList() {
        uploadCollection(vowelsCollection, vowelLetters)
    }

    suspend fun getData(): List<Consonant> {
        val productSnapshot = alphabetsCollection.orderBy("id").get().await()
        val products = productSnapshot.documents.mapNotNull { it.toObject<Consonant>() }
//        consonants.addAll(products)
        return products
    }

    private suspend inline fun <reified T : Any> uploadCollection(
        collection: CollectionReference, dataList: List<T>
    ) {
        dataList.forEach { data ->
            collection.add(data).await()
        }
    }

    private suspend fun FirebaseFirestore.deleteAllDocumentsInCollection(collection: CollectionReference): Result<Unit> {
        return try {
            val snapshot = collection.get().await()
            this.runBatch { batch ->
                snapshot.documents.forEach { document ->
                    batch.delete(document.reference)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}