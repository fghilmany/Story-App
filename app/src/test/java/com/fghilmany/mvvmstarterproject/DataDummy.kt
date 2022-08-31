package com.fghilmany.mvvmstarterproject

import com.fghilmany.mvvmstarterproject.core.data.local.entity.StoryEntity
import com.fghilmany.mvvmstarterproject.core.data.remote.response.BasicResponse
import com.fghilmany.mvvmstarterproject.core.data.remote.response.LoginResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type

object DataDummy {
    fun loginResponse(): LoginResponse {
        val json = """
            {
                "error": false,
                "message": "success",
                "loginResult": {
                    "userId": "user-33xfuCmxS8-VBFeb",
                    "name": "test123",
                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTMzeGZ1Q214UzgtVkJGZWIiLCJpYXQiOjE2NjEyMTk0NjN9.aXBuDIsqJVWm-sGClOUFgbT2xU_ROOHEojhjupCLu6Y"
                }
            }
        """.trimIndent()
        return Gson().fromJson(json, LoginResponse::class.java)
    }

    fun basicResponse() = BasicResponse(
        false,
        "User created"
    )

    fun multipartFile() = MultipartBody.Part.create("dummyFile".toRequestBody())

    fun requestBody(text: String) = text.toRequestBody()

    fun storyFromDb(): List<StoryEntity>{
        val json = """
            [
                {
                    "id": "story-jXR7BV0sVBDpnWLu",
                    "name": "ninu",
                    "description": "dhhfg",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1661863099446_tc4d3wFX.jpg",
                    "createdAt": "2022-08-30T12:38:19.448Z",
                    "lat": -7.7856193,
                    "lon": 110.46422
                },
                {
                    "id": "story-h88og1LDkqfb7bb2",
                    "name": "Dicoding",
                    "description": "Tak ada yang tak mungkin jika berusaha",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648721132156_MX2C9FYA.jpg",
                    "createdAt": "2022-03-31T10:05:32.157Z",
                    "lat": -6.4517064,
                    "lon": 107.9145669
                },
                {
                    "id": "story-MNZlDhSUrebE_xy4",
                    "name": "Dicoding",
                    "description": "Upgrade Diri saat Pandemi, Perlu!",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648721059572_iyQwKOO-.jpg",
                    "createdAt": "2022-03-31T10:04:19.574Z",
                    "lat": -2.9549663,
                    "lon": 104.6929236
                },
                {
                    "id": "story-LsJW4nIdEm5166pp",
                    "name": "Dicoding",
                    "description": "Kalau saja saya tidak mencantumkan sertifikat kompetensi dari Dicoding, mungkin hasilnya zonk. Habisnya, pengalaman kerja 5 tahun lalu gak relevan sama sekali dengan Web Developer.",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648721013029_xPquDWah.jpg",
                    "createdAt": "2022-03-31T10:03:33.032Z",
                    "lat": -6.8934518,
                    "lon": 112.0253573
                },
                {
                    "id": "story-t1br2m3Tg8QHSG5v",
                    "name": "Dicoding",
                    "description": "Berkat Kekurangan, Tak Jadi Halangan",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648720936351_XAaAyT6U.jpg",
                    "createdAt": "2022-03-31T10:02:16.352Z",
                    "lat": -6.545286,
                    "lon": 106.5338975
                },
                {
                    "id": "story-RRXghhvQZla2wUTU",
                    "name": "Dicoding",
                    "description": "Pendidikan formal dan ijazah saja tidak cukup untuk self growth",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648720873748_H78HnWtO.jpg",
                    "createdAt": "2022-03-31T10:01:13.750Z",
                    "lat": -7.9786395,
                    "lon": 112.5617421
                },
                {
                    "id": "story-ZBk-FmRMTBV71Hhf",
                    "name": "Dicoding",
                    "description": "Jadi Developer Harus Berani Keluar Zona Nyaman",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648720795526_877kLrZI.jpg",
                    "createdAt": "2022-03-31T09:59:55.530Z",
                    "lat": -7.9786395,
                    "lon": 112.5617421
                },
                {
                    "id": "story-Yk89TWuHxSYsE-1u",
                    "name": "Dicoding",
                    "description": "Because for me, in this tough times, Bangkit represents hope. Only in Bangkit..a person like me..can study with the best curriculum and best experts. Thanks to Bangkit, now I aspire to be a machine learning specialist!",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648720737599_XbEPxqHS.jpg",
                    "createdAt": "2022-03-31T09:58:57.600Z",
                    "lat": -7.4730525,
                    "lon": 110.1825048
                },
                {
                    "id": "story-daqkGOdCARqApatM",
                    "name": "Dicoding",
                    "description": "Jika sudah berusaha maksimal, saatnya berserah diri. Dengan sikap ikhlas ini, meski gagal sekalipun, tidak jadi soal. Pasti itu hal terbaik yang dipercayakan pada kita untuk saat ini.",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648720672402_JwvDCowd.jpg",
                    "createdAt": "2022-03-31T09:57:52.403Z",
                    "lat": -7.9786395,
                    "lon": 112.5617421
                },
                {
                    "id": "story-QkK0bSX6pdCS6GU7",
                    "name": "Dicoding",
                    "description": "Agar sukses (meniti karir), “belajar jangan hanya di kampus saja!",
                    "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1648720583566_jD27n82O.png",
                    "createdAt": "2022-03-31T09:56:23.567Z",
                    "lat": -6.9733165,
                    "lon": 107.6281415
                }
            ]
        """.trimIndent()
        val listStory: Type = object : TypeToken<List<StoryEntity>>() {}.type
        return Gson().fromJson(json, listStory)
    }
}