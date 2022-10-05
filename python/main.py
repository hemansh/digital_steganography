
import argparse
import math
from PIL import Image

NUM_PIXELS_TO_HIDE_LEN = 11

MASK = 0b1


def encode(file_path, text):

    if not file_path.endswith(".jpeg") and not file_path.endswith(".jpg"):
        raise InvalidImageTypeException(
            "Error: image to encode must be .jpg format")
    if not text.strip():
        raise ValueError("Input text must be non-empty and non-null.")

    before = Image.open(file_path)
    pixmap_before = before.load()

    text_bits = tobits(text)
    num_pixels = before.size[0] * before.size[1]
    if len(text_bits) > (num_pixels - 11) * 3:
        raise ValueError("Error: input text cannot fit in image.")

    after = Image.new(before.mode, before.size)
    pixmap_after = after.load()

    return encode_text(before, pixmap_before, after, pixmap_after, text_bits)


def encode_text(before, pixmap_before, after, pixmap_after, text_bits):
    num_loops = bin(int(math.ceil(len(text_bits) / 3 + 1)))
    num_loops = num_loops[2:]

    i = len(num_loops) - 1
    for x in range(NUM_PIXELS_TO_HIDE_LEN):
        r_bin, g_bin, b_bin = get_pixels_bin(
            pixmap_before, before.size[0] - x - 1, before.size[1] - 1)

        if i >= 0:
            b_bin = set_bit(
                b_bin, 0) if num_loops[i] == '1' else clear_bit(b_bin, 0)
            i -= 1
        else:
            b_bin = clear_bit(b_bin, 0)
        if i >= 0:
            g_bin = set_bit(
                g_bin, 0) if num_loops[i] == '1' else clear_bit(g_bin, 0)
            i -= 1
        else:
            g_bin = clear_bit(g_bin, 0)
        if i >= 0:
            r_bin = set_bit(
                r_bin, 0) if num_loops[i] == '1' else clear_bit(r_bin, 0)
            i -= 1
        else:
            r_bin = clear_bit(r_bin, 0)

        pixmap_after[before.size[0] - x - 1,
                     before.size[1] - 1] = (r_bin, g_bin, b_bin)

    i = 0
    for y in range(before.size[1]):
        for x in range(before.size[0]):

            if y == before.size[1] - 1 and x == before.size[0] - NUM_PIXELS_TO_HIDE_LEN:
                break

            r_bin, g_bin, b_bin = get_pixels_bin(pixmap_before, x, y)
            if i < len(text_bits):
                r_bin = set_bit(
                    r_bin, 0) if text_bits[i] == 1 else clear_bit(r_bin, 0)
                i += 1
            if i < len(text_bits):
                g_bin = set_bit(
                    g_bin, 0) if text_bits[i] == 1 else clear_bit(g_bin, 0)
                i += 1
            if i < len(text_bits):
                b_bin = set_bit(
                    b_bin, 0) if text_bits[i] == 1 else clear_bit(b_bin, 0)
                i += 1

            pixmap_after[x, y] = (r_bin, g_bin, b_bin)

    return after


def decode(file_path):
    if not file_path.endswith(".png"):
        raise InvalidImageTypeException(
            "Error: image to decode must be .png format.")

    img = Image.open(file_path)
    pixmap = img.load()

    bits = ''
    for x in range(NUM_PIXELS_TO_HIDE_LEN):
        r_bin, g_bin, b_bin = get_pixels_bin(
            pixmap, img.size[0] - x - 1, img.size[1] - 1)
        bits = str(b_bin & MASK) + bits
        bits = str(g_bin & MASK) + bits
        bits = str(r_bin & MASK) + bits

    loop_count = int(bits, 2)

    result = []
    index = 0
    for y in range(img.size[1]):
        for x in range(img.size[0]):

            if index == loop_count:
                break

            r_bin, g_bin, b_bin = get_pixels_bin(pixmap, x, y)
            result.append(r_bin & MASK)
            result.append(g_bin & MASK)
            result.append(b_bin & MASK)

            index += 1

    return frombits(result)


def get_pixels_bin(pixmap, x, y):
    r, g, b = pixmap[x, y]
    #print(pixmap[x, y])
    r_bin = int(bin(r), 2)
    g_bin = int(bin(g), 2)
    b_bin = int(bin(b), 2)
    return r_bin, g_bin, b_bin


def set_bit(value, bit):
    return value | (1 << bit)


def clear_bit(value, bit):
    return value & ~(1 << bit)


def tobits(s):
    result = []
    for c in s:
        bits = bin(ord(c))[2:]
        bits = '00000000'[len(bits):] + bits
        result.extend([int(b) for b in bits])
    return result


def frombits(bits):
    chars = []
    for b in range(int(len(bits) / 8)):
        byte = bits[b*8:(b+1)*8]
        chars.append(chr(int(''.join([str(bit) for bit in byte]), 2)))
    return ''.join(chars)


class InvalidImageTypeException(ValueError):
    def __init__(self, message):
        super().__init__(message)


class IllegalArgumentError(ValueError):
    def __init__(self, message):
        super().__init__(message)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        usage='./PySteg [path] [-e string] [-d] [-h]')
    parser.add_argument('path', help='The relative path of the image to use')
    parser.add_argument('-e', '--encode', help='Text to encode in an image')
    parser.add_argument(
        '-d', '--decode', help='Decode from an image', action='store_true')
    args = parser.parse_args()

    if args.encode:
        encode(args.path, args.encode).save('output.png')
    elif args.decode:
        print(decode(args.path))
