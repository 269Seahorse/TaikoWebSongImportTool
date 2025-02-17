package com.alebit.taikoweb

import com.alebit.taikoweb.parser.parseGenre
import com.alebit.taikoweb.parser.parseTJA
import com.alebit.taikoweb.struct.Genre
import com.alebit.taikoweb.struct.TaikoWebSong
import java.nio.file.Path
import kotlin.collections.ArrayList

fun loadTJAs(source: Path, category: Genre = Genre.None, similarFile: Boolean = false, utf8: Boolean = false): ArrayList<TaikoWebSong> {
    val songs = ArrayList<TaikoWebSong>();
    loadTJAs(source, songs, category, similarFile, utf8);
    songs.sort();
    return songs;
}

private fun loadTJAs(source: Path, songs: ArrayList<TaikoWebSong>, category: Genre = Genre.None, similarFile: Boolean = false, utf8: Boolean = false) {
    val genreFile = source.resolve("genre.ini").toFile();
    var genre: Genre = category;
    if (source.resolve("genre.ini").toFile().exists()) {
        val newCategory = parseGenre(genreFile);
        if (newCategory != Genre.None) {
            genre = newCategory;
        }
    }
    val files = source.toFile().listFiles();
    for (file in files) {
        if (file.isDirectory) {
            loadTJAs(file.toPath(), songs, genre, similarFile, utf8);
        } else if (file.extension.toLowerCase().equals("tja")) {
            println("Load TJA: " + file.absolutePath);
            val song = parseTJA(file, genre, similarFile, utf8);
            if (song.title == null || song.musicFile == null) {
                System.err.println("Could not parse TJA file \"" + file.absolutePath + "\"");
                continue;
            }
            songs.add(song);
        }
    }
}
