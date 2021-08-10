package com.github.jmlb23.marvel.data


import com.github.jmlb23.marvel.BuildKonfig
import kotlinx.datetime.Clock
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sin

/**
 * Helper methods to encode as MD5
 * @see <a href="https://github.com/korlibs/kmem">korlibs/kmem</a>
 */
internal fun Int.rotateLeft(bits: Int): Int = ((this shl bits) or (this ushr (32 - bits)))

internal fun arraycopy(src: ByteArray, srcPos: Int, dst: ByteArray, dstPos: Int, count: Int) =
    src.copyInto(dst, dstPos, srcPos, srcPos + count)

internal object Hex {
    private const val DIGITS = "0123456789ABCDEF"
    private val DIGITS_UPPER = DIGITS.toUpperCase()
    private val DIGITS_LOWER = DIGITS.toLowerCase()

    private fun decodeHexDigit(c: Char): Int = when (c) {
        in '0'..'9' -> c - '0'
        in 'a'..'f' -> (c - 'a') + 10
        in 'A'..'F' -> (c - 'A') + 10
        else -> error("Invalid hex digit '$c'")
    }

    operator fun invoke(v: String) = decode(v)
    operator fun invoke(v: ByteArray) = encode(v)

    fun decode(str: String): ByteArray {
        val out = ByteArray(str.length / 2)
        var m = 0
        for (n in out.indices) {
            val c0 = decodeHexDigit(str[m++])
            val c1 = decodeHexDigit(str[m++])
            out[n] = ((c0 shl 4) or c1).toByte()
        }
        return out
    }

    fun encode(src: ByteArray): String = encodeBase(src, DIGITS_LOWER)
    fun encodeLower(src: ByteArray): String = encodeBase(src, DIGITS_LOWER)
    fun encodeUpper(src: ByteArray): String = encodeBase(src, DIGITS_UPPER)

    private fun encodeBase(data: ByteArray, digits: String = DIGITS): String = buildString(data.size * 2) {
        for (n in data.indices) {
            val v = data[n].toInt() and 0xFF
            append(digits[(v ushr 4) and 0xF])
            append(digits[(v ushr 0) and 0xF])
        }
    }
}

internal fun String.fromHex(): ByteArray = Hex.decode(this)
internal val ByteArray.hexLower: String get() = Hex.encodeLower(this)
internal val ByteArray.hexUpper: String get() = Hex.encodeUpper(this)
internal val ByteArray.hex: String get() = Hex.encodeLower(this)

internal object Base64 {
    private val TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
    private val DECODE = IntArray(0x100).apply {
        for (n in 0..255) this[n] = -1
        for (n in 0 until TABLE.length) {
            this[TABLE[n].toInt()] = n
        }
    }

    operator fun invoke(v: String) = decodeIgnoringSpaces(v)
    operator fun invoke(v: ByteArray) = encode(v)

    fun decode(str: String): ByteArray {
        val src = ByteArray(str.length) { str[it].code.toByte() }
        val dst = ByteArray(src.size)
        return dst.copyOf(decode(src, dst))
    }

    fun decodeIgnoringSpaces(str: String): ByteArray {
        return decode(str.replace(" ", "").replace("\n", "").replace("\r", ""))
    }

    fun decode(src: ByteArray, dst: ByteArray): Int {
        var m = 0

        var n = 0
        while (n < src.size) {
            val d = DECODE[src.readU8(n)]
            if (d < 0) {
                n++
                continue // skip character
            }

            val b0 = DECODE[src.readU8(n++)]
            val b1 = DECODE[src.readU8(n++)]
            val b2 = DECODE[src.readU8(n++)]
            val b3 = DECODE[src.readU8(n++)]
            dst[m++] = (b0 shl 2 or (b1 shr 4)).toByte()
            if (b2 < 64) {
                dst[m++] = (b1 shl 4 or (b2 shr 2)).toByte()
                if (b3 < 64) {
                    dst[m++] = (b2 shl 6 or b3).toByte()
                }
            }
        }
        return m
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    fun encode(src: ByteArray): String {
        val out = StringBuilder((src.size * 4) / 3 + 4)
        var ipos = 0
        val extraBytes = src.size % 3
        while (ipos < src.size - 2) {
            val num = src.readU24BE(ipos)
            ipos += 3

            out.append(TABLE[(num ushr 18) and 0x3F])
            out.append(TABLE[(num ushr 12) and 0x3F])
            out.append(TABLE[(num ushr 6) and 0x3F])
            out.append(TABLE[(num ushr 0) and 0x3F])
        }

        if (extraBytes == 1) {
            val num = src.readU8(ipos++)
            out.append(TABLE[num ushr 2])
            out.append(TABLE[(num shl 4) and 0x3F])
            out.append('=')
            out.append('=')
        } else if (extraBytes == 2) {
            val tmp = (src.readU8(ipos++) shl 8) or src.readU8(ipos++)
            out.append(TABLE[tmp ushr 10])
            out.append(TABLE[(tmp ushr 4) and 0x3F])
            out.append(TABLE[(tmp shl 2) and 0x3F])
            out.append('=')
        }

        return out.toString()
    }

    private fun ByteArray.readU8(index: Int): Int = this[index].toInt() and 0xFF
    private fun ByteArray.readU24BE(index: Int): Int =
        (readU8(index + 0) shl 16) or (readU8(index + 1) shl 8) or (readU8(index + 2) shl 0)
}

internal fun String.fromBase64(ignoreSpaces: Boolean = false): ByteArray =
    if (ignoreSpaces) Base64.decodeIgnoringSpaces(this) else Base64.decode(this)

internal val ByteArray.base64: String get() = Base64.encode(this)

internal open class HasherFactory(val create: () -> Hasher) {
    fun digest(data: ByteArray) = create().also { it.update(data, 0, data.size) }.digest()
}

internal abstract class Hasher(val chunkSize: Int, val digestSize: Int) {
    private val chunk = ByteArray(chunkSize)
    private var writtenInChunk = 0
    private var totalWritten = 0L

    fun reset(): Hasher {
        coreReset()
        writtenInChunk = 0
        totalWritten = 0L
        return this
    }

    fun update(data: ByteArray, offset: Int, count: Int): Hasher {
        var curr = offset
        var left = count
        while (left > 0) {
            val remainingInChunk = chunkSize - writtenInChunk
            val toRead = min(remainingInChunk, left)
            arraycopy(data, curr, chunk, writtenInChunk, toRead)
            left -= toRead
            curr += toRead
            writtenInChunk += toRead
            if (writtenInChunk >= chunkSize) {
                writtenInChunk -= chunkSize
                coreUpdate(chunk)
            }
        }
        totalWritten += count
        return this
    }

    fun digestOut(out: ByteArray) {
        val pad = corePadding(totalWritten)
        var padPos = 0
        while (padPos < pad.size) {
            val padSize = chunkSize - writtenInChunk
            arraycopy(pad, padPos, chunk, writtenInChunk, padSize)
            coreUpdate(chunk)
            writtenInChunk = 0
            padPos += padSize
        }

        coreDigest(out)
        coreReset()
    }

    protected abstract fun coreReset()
    protected abstract fun corePadding(totalWritten: Long): ByteArray
    protected abstract fun coreUpdate(chunk: ByteArray)
    protected abstract fun coreDigest(out: ByteArray)

    fun update(data: ByteArray) = update(data, 0, data.size)
    fun digest(): Hash = Hash(ByteArray(digestSize).also { digestOut(it) })
}

internal class Hash(private val bytes: ByteArray) {
    companion object {
        fun fromHex(hex: String): Hash = Hash(Hex.decode(hex))
        fun fromBase64(base64: String): Hash = Hash(Base64.decodeIgnoringSpaces(base64))
    }

    val base64 get() = Base64.encode(bytes)
    val hex get() = Hex.encode(bytes)
    val hexLower get() = Hex.encodeLower(bytes)
    val hexUpper get() = Hex.encodeUpper(bytes)
}

internal fun ByteArray.hash(algo: HasherFactory): Hash = algo.digest(this)

internal class MD5 : Hasher(chunkSize = 64, digestSize = 16) {
    companion object : HasherFactory({ MD5() }) {
        private val S = intArrayOf(7, 12, 17, 22, 5, 9, 14, 20, 4, 11, 16, 23, 6, 10, 15, 21)
        private val T = IntArray(64) { ((1L shl 32) * abs(sin(1.0 + it))).toLong().toInt() }
    }

    private val r = IntArray(4)
    private val o = IntArray(4)
    private val b = IntArray(16)

    init {
        coreReset()
    }

    override fun coreReset() {
        r[0] = 0x67452301
        r[1] = 0xEFCDAB89.toInt()
        r[2] = 0x98BADCFE.toInt()
        r[3] = 0x10325476
    }

    override fun coreUpdate(chunk: ByteArray) {
        for (j in 0 until 64) b[j ushr 2] = (chunk[j].toInt() shl 24) or (b[j ushr 2] ushr 8)
        for (j in 0 until 4) o[j] = r[j]
        for (j in 0 until 64) {
            val d16 = j / 16
            val f = when (d16) {
                0 -> (r[1] and r[2]) or (r[1].inv() and r[3])
                1 -> (r[1] and r[3]) or (r[2] and r[3].inv())
                2 -> r[1] xor r[2] xor r[3]
                3 -> r[2] xor (r[1] or r[3].inv())
                else -> 0
            }
            val bi = when (d16) {
                0 -> j
                1 -> (j * 5 + 1) and 0x0F
                2 -> (j * 3 + 5) and 0x0F
                3 -> (j * 7) and 0x0F
                else -> 0
            }
            val temp = r[1] + (r[0] + f + b[bi] + T[j]).rotateLeft(S[(d16 shl 2) or (j and 3)])
            r[0] = r[3]
            r[3] = r[2]
            r[2] = r[1]
            r[1] = temp
        }
        for (j in 0 until 4) r[j] += o[j]
    }

    override fun corePadding(totalWritten: Long): ByteArray {
        val numberOfBlocks = ((totalWritten + 8) / chunkSize) + 1
        val totalWrittenBits = totalWritten * 8
        return ByteArray(((numberOfBlocks * chunkSize) - totalWritten).toInt()).apply {
            this[0] = 0x80.toByte()
            for (i in 0 until 8) this[this.size - 8 + i] = (totalWrittenBits ushr (8 * i)).toByte()
        }
    }

    override fun coreDigest(out: ByteArray) {
        for (it in 0 until 16) out[it] = (r[it / 4] ushr ((it % 4) * 8)).toByte()
    }
}

internal fun ByteArray.md5() = hash(MD5)
internal fun String.md5(): String = encodeToByteArray().md5().hex

fun String.withParams(params: Map<String, String> = mapOf()): String {
    val millis = Clock.System.now().toEpochMilliseconds() / 1000
    val hash = "${millis}${BuildKonfig.API_KEY_PRIVATE}${BuildKonfig.API_KEY}".md5()
    return "$this?ts=${millis}&apikey=${BuildKonfig.API_KEY}&hash=${hash}".let {
        if (params.isEmpty()) it
        else "$it&${params.asIterable().joinToString("&") { "${it.key}=${it.value}" }}"
    }
}
