package dev.nokee.init.internal.wrapper;

import lombok.val;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GradleWrapperScriptPatcher {
	private final String content;
	private String pill;
	private List<String> contentToInject;
	private String pillPrefix;

	private GradleWrapperScriptPatcher(String content) {
		this.content = content;
	}

	public static GradleWrapperScriptPatcher forContent(String content) {
		return new GradleWrapperScriptPatcher(content);
	}

	public GradleWrapperScriptPatcher findLineWith(String pill) {
		this.pill = pill;
		return this;
	}

	public GradleWrapperScriptPatcher injectBefore(List<String> contentToInject) {
		this.contentToInject = contentToInject;
		return this;
	}

	public GradleWrapperScriptPatcher andPrependPillWith(String pillPrefix) {
		this.pillPrefix = pillPrefix;
		return this;
	}

	public List<String> patch() {
		try (val reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))))) {
			return Stream.concat(reader.lines().flatMap(it -> {
				if (it.contains(pill)) {
					return Stream.concat(contentToInject.stream(), Stream.of(it.replace(pill, pillPrefix + pill)));
				}
				return Stream.of(it);
			}), /* for lost of end-of-file newline */ Stream.of("")).collect(Collectors.toList());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
